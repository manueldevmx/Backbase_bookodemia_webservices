package mx.kodemia.bookodemia.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import mx.kodemia.bookodemia.*
import mx.kodemia.bookodemia.model.DataClassHome

class RecyclerViewHome ( val home: MutableList<DataClassHome>, val context: Context): RecyclerView.Adapter<RecyclerViewHome.HomeHolder>() {

    class HomeHolder (val view: View) : RecyclerView.ViewHolder(view){
        val cardView: MaterialCardView = view.findViewById(R.id.cardView_item_home)
        val textViewTitulo: TextView = view.findViewById(R.id.text_title)
        val textViewAutor: TextView = view.findViewById(R.id.text_autor)
        val textViewCategoria: TextView = view.findViewById(R.id.text_categoria)

        fun render(home: DataClassHome, context: Context){
            textViewTitulo.setText(home.tituloLibro)
            textViewAutor.setText(home.autorLibro)
            textViewCategoria.setText(home.categoriaLibro)

            cardView.setOnClickListener {
                Snackbar.make(view, "Acceso a ${home.tituloLibro}",Snackbar.LENGTH_SHORT).show()
                val intent: Intent = Intent(context,DetailsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHome.HomeHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeHolder(layoutInflater.inflate(R.layout.item_cardview_home,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHome.HomeHolder, position: Int) {
        holder.render(home[position], context)
    }

    override fun getItemCount(): Int = home.size
}