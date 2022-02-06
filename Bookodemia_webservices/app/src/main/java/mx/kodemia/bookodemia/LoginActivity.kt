package mx.kodemia.bookodemia

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.extra.*
import mx.kodemia.bookodemia.model.Errors
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private val TAG = LoginActivity::class.qualifiedName
    private val name_device: String = android.os.Build.MANUFACTURER

    override fun onCreate(savedInstanceState: Bundle?) {
        eliminarSesion(applicationContext)
        if(validarSesion(applicationContext)){
            lanzarActivity()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        tietPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editPass: Editable?) {
                if(editPass.toString().trim().isEmpty()){
                    tilPassword.setError("Contraseña requerida")
                }else{
                    tilPassword.setErrorEnabled(false)
                    tilPassword.setError("")
                }
            }

        })

        tietCorreo.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editText: Editable?) {
                if(editText.toString().trim().isEmpty()){
                    tilCorreo.setError("Correo requerido")
                }else{
                    tilCorreo.setErrorEnabled(false)
                    tilCorreo.setError("")
                }
            }

        })
    }

    fun lanzarActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun init(){
        bilLog.setOnClickListener {
            val listaBool = listOf<Boolean>(validarCorreo(),validarContrasena())
            var contador = 0
            for(validacion in listaBool){
                if(validacion == false){
                    contador++
                }
            }
            if(contador<1){
                contador = 0
                realizarPeticion()
            }
        }
        val textSigin = findViewById<TextView>(R.id.textSigin)
        textSigin.setOnClickListener {
            startActivity(Intent(this,SiginActivity::class.java))
        }
    }

    private fun validarCorreo(): Boolean{
        return if(tietCorreo.text.toString().isEmpty()){
            tilCorreo.error = getString(R.string.campo_vacio)
            false
        }else{
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(tietCorreo.text.toString()).matches()){
                tilCorreo.isErrorEnabled = false
                true
            }else{
                tilCorreo.error = getString(R.string.error_correo)
                false
            }
        }
    }

    private fun validarContrasena(): Boolean{
        return if(tietPassword.text.toString().isEmpty()){
            tilPassword.error = getString(R.string.campo_vacio)
            false
        }else{
            tilPassword.isErrorEnabled = false
            true
        }
    }

    fun realizarPeticion(){
        VolleyLog.DEBUG = true
        if(estaEnLinea(applicationContext)){
            bilLog.visibility = View.GONE
            pb_login.visibility = View.VISIBLE
            val cola = Volley.newRequestQueue(applicationContext)
            val JsonObj: JSONObject = JSONObject()
            JsonObj.put("email", tietCorreo.text)
            JsonObj.put("password", tietPassword.text)
            JsonObj.put("device_name", "Fabian's phone")
            val peticion = object: JsonObjectRequest(Request.Method.POST,getString(R.string.url_servidor)+getString(R.string.api_login),JsonObj, Response.Listener {
                    response ->
                val json = JSONObject(response.toString())
                iniciarSesion(applicationContext,json)
                if(validarSesion(applicationContext)){
                    lanzarActivity()
                }
            },{
                    error ->
                bilLog.visibility = View.VISIBLE
                pb_login.visibility = View.GONE
                /*if(error.networkResponse.statusCode == 422){
                    // Realizamos una accion muy especifica para el error de 422
                }*/
                val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                //val errors = Json.decodeFromString<Errors>(json.toString())
                val errors = Gson().fromJson(json.toString(),Errors::class.java)
                for (error in errors.errors){
                    mensajeEmergente(this,error.detail)
                }
                Log.e(TAG, error.networkResponse.toString())
                Log.e(TAG, error.toString())
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            cola.add(peticion)
        }else{
            mensajeEmergente(this,getString(R.string.error_internet))
        }
    }
}