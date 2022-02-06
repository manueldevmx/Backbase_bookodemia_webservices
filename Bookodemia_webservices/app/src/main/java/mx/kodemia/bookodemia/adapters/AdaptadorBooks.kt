package mx.kodemia.bookodemia.adapters

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import mx.kodemia.bookodemia.*
import mx.kodemia.bookodemia.Connections.Connect
import mx.kodemia.bookodemia.extra.eliminarSesion
import mx.kodemia.bookodemia.extra.estaEnLinea
import mx.kodemia.bookodemia.extra.mensajeEmergente
import mx.kodemia.bookodemia.extra.obtenerkDeSesion
import mx.kodemia.bookodemia.model.Errors
import mx.kodemia.bookodemia.modelAuthors.DataCategory
import mx.kodemia.bookodemia.modelAuthors.DataFirstAuthor
import mx.kodemia.bookodemia.modelAuthors.DataSecAuthor
import mx.kodemia.bookodemia.modelBooks.Data
import mx.kodemia.bookodemia.modelBooks.datosLibro
import org.json.JSONObject

class AdaptadorBooks(val activity: Activity, val books: MutableList<datosLibro>): RecyclerView.Adapter<AdaptadorBooks.BookHolder>() {

    private val TAG = HomeFragment::class.qualifiedName
    private var authorConnect = Connect()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview_home,parent,false)
        return BookHolder(view)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val book = books.get(position)
        with(holder){
            cardView.setOnClickListener {
                val bundle = Bundle()
                val bundle2 = Bundle()
                val bundle3 = Bundle()
                bundle.putSerializable("book",book)
                bundle2.putString("author",tv_autor.text.toString())
                bundle3.putString("category", tv_category.text.toString())
                val intent = Intent(activity,DetailsActivity::class.java)
                intent.putExtras(bundle)
                intent.putExtras(bundle2)
                intent.putExtras(bundle3)
                activity.startActivity(intent)
            }
            tv_title.text = book.attributes.title
            //tv_autor.text = book.relationships.authors.links.self
            //tv_category.text = book.relationships.categories.links.related

            realizarPeticion(book.id,tv_autor)
            realizarPeticionDos(book.id,tv_category)
        }
    }

    override fun getItemCount(): Int = books.size

    class BookHolder(view: View): RecyclerView.ViewHolder(view){
        val cardView: MaterialCardView = view.findViewById(R.id.cardView_item_home)
        val tv_title: TextView = view.findViewById(R.id.text_title)
        val tv_autor: TextView = view.findViewById(R.id.text_autor)
        val tv_category: TextView = view.findViewById(R.id.text_categoria)
    }

    fun realizarPeticion(id_libro: String, tv_autor: TextView){
        if(estaEnLinea(activity)){
            val cola = Volley.newRequestQueue(activity)
            val peticion = object: JsonObjectRequest(
                Request.Method.GET,"https://playground-bookstore.herokuapp.com/api/v1/books/"+id_libro+"/authors",null,
                { response ->
                    Log.d(TAG,response.toString())
                    // val books = Json.decodeFromString<Data>(response.toString())
                    val author = Gson().fromJson(response.toString(),DataSecAuthor::class.java)
                    tv_autor.text = author.data.attributes.name
                },{
                        error ->
                    if(error.networkResponse.statusCode == 429){
                        //mensajeEmergente(activity, "Respuestas del servidor agotadas")
                    }else {
                        val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                        //val errors = Json.decodeFromString<Errors>(json.toString())
                        val errors = Gson().fromJson(json.toString(), Errors::class.java)
                        for (error in errors.errors) {
                            mensajeEmergente(activity, error.detail)
                        }
                    }
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Authorization"] = "Bearer ${obtenerkDeSesion(activity,"token")}"
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            cola.add(peticion)
        }
        else{
            mensajeEmergente(activity,"No tienes internet Karnal")
        }
    }

    fun realizarPeticionDos(id_libro: String, tv_category: TextView){
        if(estaEnLinea(activity)){
            val cola = Volley.newRequestQueue(activity)
            val peticion = object: JsonObjectRequest(
                Request.Method.GET,"https://playground-bookstore.herokuapp.com/api/v1/books/"+id_libro+"/categories",null,
                { response ->
                    Log.d(TAG,response.toString())
                    // val books = Json.decodeFromString<Data>(response.toString())
                    val category = Gson().fromJson(response.toString(),DataCategory::class.java)
                    tv_category.text = category.data.attributes.name
                },{
                        error ->
                    if(error.networkResponse.statusCode == 429){
                        //mensajeEmergente(activity, "Respuestas del servidor agotadas")
                    }else {
                        val json = JSONObject(String(error.networkResponse.data, Charsets.UTF_8))
                        //val errors = Json.decodeFromString<Errors>(json.toString())
                        val errors = Gson().fromJson(json.toString(), Errors::class.java)
                        for (error in errors.errors) {
                            mensajeEmergente(activity, error.detail)
                        }
                    }
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Authorization"] = "Bearer ${obtenerkDeSesion(activity,"token")}"
                    headers["Accept"] = "application/json"
                    headers["Content-type"] = "application/json"
                    return headers
                }
            }
            cola.add(peticion)
        }
        else{
            mensajeEmergente(activity,"No tienes internet Karnal")
        }
    }
}