package mx.kodemia.bookodemia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sigin.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.extra.estaEnLinea
import mx.kodemia.bookodemia.extra.iniciarSesion
import mx.kodemia.bookodemia.extra.mensajeEmergente
import mx.kodemia.bookodemia.model.Errors
import org.json.JSONObject

class SiginActivity : AppCompatActivity() {

    private val TAG = SiginActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sigin)
        init()

        textReturnFromSigin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        tietUsuario.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editUser: Editable?) {
                if(editUser.toString().trim().isEmpty()){
                    tilUsuario.setError("Usuario requerido")
                }else{
                    tilUsuario.setErrorEnabled(false)
                    tilUsuario.setError("")
                }
            }

        })

        tietCorreoSigin.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editCorreo: Editable?) {
                if(editCorreo.toString().trim().isEmpty()){
                    tilCorreoSigin.setError("Correo requerido")
                }else{
                    tilCorreoSigin.setErrorEnabled(false)
                    tilCorreoSigin.setError("")
                }
            }

        })

        tietPasswordSigin.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editPass: Editable?) {
                if(editPass.toString().trim().isEmpty()){
                    tilPasswordSigin.setError("Contraseña requerida")
                }else{
                    tilPasswordSigin.setErrorEnabled(false)
                    tilPasswordSigin.setError("")
                }
            }

        })

        tietPassConfirm.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editConfirm: Editable?) {
                if(editConfirm.toString().trim().isEmpty()){
                    tilPassConfirm.setError("Confirmación requerida")
                }else{
                    tilPassConfirm.setErrorEnabled(false)
                    tilPassConfirm.setError("")
                }
            }

        })
    }

    fun init(){
        bilSigin.setOnClickListener {
            val listaBool = listOf<Boolean>(validarNombre(),validarCorreo(),validarContrasena(),validarContrasenaDos(),validarSimilutud())
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
    }

    private fun validarCorreo(): Boolean{
        return if(tietCorreoSigin.text.toString().isEmpty()){
            tilCorreoSigin.error = getString(R.string.campo_vacio)
            false
        }else{
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(tietCorreoSigin.text.toString()).matches()){
                tilCorreoSigin.isErrorEnabled = false
                true
            }else{
                tilCorreoSigin.error = getString(R.string.error_correo)
                false
            }
        }
    }

    private fun validarContrasena(): Boolean{
        return if(tietPasswordSigin.text.toString().isEmpty()){
            tilPasswordSigin.error = getString(R.string.campo_vacio)
            false
        }else{
            tilPasswordSigin.isErrorEnabled = false
            true
        }
    }

    private fun validarContrasenaDos(): Boolean{
        return if(tietPassConfirm.text.toString().isEmpty()){
            tilPassConfirm.error = getString(R.string.campo_vacio)
            false
        }else{
            tilPassConfirm.isErrorEnabled = false
            true
        }
    }

    private fun validarNombre(): Boolean{
        return if(tietUsuario.text.toString().isEmpty()){
            tilUsuario.error = getString(R.string.campo_vacio)
            false
        }else{
            tilUsuario.isErrorEnabled = false
            true
        }
    }

    private fun validarSimilutud(): Boolean{
        val textoPassword: String = tietPasswordSigin.text?.trim().toString()
        val textoPassConfirm: String = tietPassConfirm.text?.trim().toString()
        return if (textoPassword != textoPassConfirm){
            Toast.makeText(this,"Contraseñas diferentes", Toast.LENGTH_SHORT).show()
            tietPasswordSigin.setText("")
            tietPassConfirm.setText("")
            false
        }else{
            true
        }
    }

    fun realizarPeticion(){
        if(estaEnLinea(applicationContext)){
            val json = JSONObject()
            json.put("name", tietUsuario.text)
            json.put("email", tietCorreoSigin.text)
            json.put("password", tietPasswordSigin.text)
            json.put("password_confirmation", tietPassConfirm.text)
            json.put("device_name","OtroVato's phone")
            val cola = Volley.newRequestQueue(applicationContext)
            val peticion = object: JsonObjectRequest(Request.Method.POST,getString(R.string.url_servidor)+getString(R.string.api_registro),json, {
                    response ->
                Log.d(TAG,response.toString())
                val jsonObject = JSONObject(response.toString())
                iniciarSesion(applicationContext,jsonObject)
                val intent = Intent(this,HomeActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            },{
                    error ->
                if(error.networkResponse.statusCode == 422){
                    mensajeEmergente(this,getString(R.string.error_422))
                }else{
                    val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                    //val errors = Json.decodeFromString<Errors>(json.toString())
                    val errors = Gson().fromJson(json.toString(),Errors::class.java)
                    for (error in errors.errors) {
                        mensajeEmergente(this, error.detail)
                    }
                }
                Log.e(TAG,error.toString())
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