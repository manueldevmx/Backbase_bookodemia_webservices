package mx.kodemia.bookodemia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_user.*
import mx.kodemia.bookodemia.extra.eliminarSesion
import mx.kodemia.bookodemia.extra.obtenerkDeSesion

class UserFragment : Fragment(R.layout.fragment_user) {

    private val TAG = UserFragment::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        text_Logout.setOnClickListener {
            val cola = Volley.newRequestQueue(requireActivity())
            val peticion = object: StringRequest(Request.Method.POST,getString(R.string.url_servidor)+getString(R.string.api_logout),{
                    response ->
                Log.d(TAG, "Todo salio bien")
                eliminarSesion(requireActivity())
                startActivity(Intent(requireActivity(),LoginActivity::class.java))
                requireActivity().finish()
            },{
                    error ->
                Log.e(TAG, error.toString())
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Authorization"] = "Bearer ${obtenerkDeSesion(requireActivity(),"token")}"
                    return headers
                }
            }
            cola.add(peticion)
        }
        Log.e(TAG, "token: ${obtenerkDeSesion(requireActivity(),"token")}")
    }

}