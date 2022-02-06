package mx.kodemia.bookodemia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mx.kodemia.bookodemia.adapters.AdaptadorBooks
import mx.kodemia.bookodemia.adapters.RecyclerViewHome
import mx.kodemia.bookodemia.extra.eliminarSesion
import mx.kodemia.bookodemia.extra.estaEnLinea
import mx.kodemia.bookodemia.extra.mensajeEmergente
import mx.kodemia.bookodemia.extra.obtenerkDeSesion
import mx.kodemia.bookodemia.model.DataClassHome
import mx.kodemia.bookodemia.model.Errors
import mx.kodemia.bookodemia.modelAuthors.DataFirstAuthor
import mx.kodemia.bookodemia.modelBooks.Data
import org.json.JSONObject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val TAG = HomeFragment::class.qualifiedName
    val listLibros: MutableList<DataClassHome> = mutableListOf()
    var adapterHome = activity?.let { RecyclerViewHome(listLibros, requireActivity()) }
    var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent_view = view.findViewById(android.R.id.content)
        //initRecyclerHome()
    }

    private fun initRecyclerHome(){
        listLibros.add(DataClassHome("Mil años de muerte","Pancho","Terror"))
        listLibros.add(DataClassHome("La naranja mecanica","Romero","Erotico"))
        listLibros.add(DataClassHome("Los hornos de Hitler","Mariano","Relato"))
        listLibros.add(DataClassHome("Infraestructocho","Alan","Comedia"))

        recyclerView_Home.layoutManager = LinearLayoutManager(activity)
        recyclerView_Home.setHasFixedSize(true)
        adapterHome = activity?.let { RecyclerViewHome(listLibros, requireActivity()) }
        recyclerView_Home.adapter = adapterHome
    }

    override fun onResume() {
        super.onResume()
        realizarPeticion()
    }

    fun realizarPeticion(){
        if(estaEnLinea(requireActivity())){
            val cola = Volley.newRequestQueue(requireActivity())
            val peticion = object: JsonObjectRequest(
                Request.Method.GET,getString(R.string.url_servidor)+getString(R.string.api_libros),null,
                { response ->
                    Log.d(TAG,response.toString())
                    // val books = Json.decodeFromString<Data>(response.toString())
                    val books = Gson().fromJson(response.toString(),Data::class.java)
                    val adaptador = AdaptadorBooks(requireActivity(),books.data)
                    recyclerView_Home.layoutManager = LinearLayoutManager(requireActivity())
                    recyclerView_Home.adapter = adaptador
                    adaptador.notifyDataSetChanged()
                    pb_books.visibility = View.GONE
                    recyclerView_Home.visibility = View.VISIBLE
                },{
                        error ->
                    if(error.networkResponse.statusCode == 401) {
                        eliminarSesion(requireActivity())
                        val intent = Intent(requireActivity(), LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }else if(error.networkResponse.statusCode == 429){
                        realizarPeticion()
                    }else{
                        val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                        //val errors = Json.decodeFromString<Errors>(json.toString())
                        val errors = Gson().fromJson(json.toString(),Errors::class.java)
                        for (error in errors.errors){
                            mensajeEmergente(requireActivity(),error.detail)
                        }
                        recyclerView_Home.visibility = View.GONE
                        pb_books.visibility = View.VISIBLE
                    }
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Authorization"] = "Bearer ${obtenerkDeSesion(requireActivity(),"token")}"
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            cola.add(peticion)
        }
        else{
            mensajeEmergente(requireActivity(),getString(R.string.error_internet))
        }
    }
}