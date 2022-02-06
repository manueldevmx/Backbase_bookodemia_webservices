package mx.kodemia.bookodemia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_details.*
import mx.kodemia.bookodemia.modelBooks.datosLibro

class DetailsActivity : AppCompatActivity() {
    private val TAG = DetailsActivity::class.qualifiedName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        intent.extras?.let{
            val book = it.getSerializable("book") as datosLibro
            text_mockup.text = getString(R.string.Mockupchange,book.attributes.title)
            textView12.text = getString(R.string.lorem_Ipsum_Des,book.attributes.content)
            Log.d(TAG,book.attributes.title)
            val author = it.getString("author")
            text_autor_details.text = getString(R.string.Autorchange,author)
            val category = it.getString("category")
            text_category_details.text = getString(R.string.Categoriachange,category)
        }

        val button_favorite: ImageButton = findViewById(R.id.imageButton7)
        val button_share: ImageButton = findViewById(R.id.imageButton6)
        button_favorite.setBackgroundResource(0)
        button_share.setBackgroundResource(0)

        text_return.setOnClickListener{
            startActivity(Intent(this,HomeActivity::class.java))
        }

        val text_button_des: TextView = findViewById(R.id.textView6)
        val text_button_det: TextView = findViewById(R.id.textView10)
        val alter_text: TextView = findViewById(R.id.textView12)

        text_button_des.setOnClickListener {
            intent.extras?.let{
                val book = it.getSerializable("book") as datosLibro
                alter_text.text = getString(R.string.lorem_Ipsum_Des,book.attributes.content)
            }
        }

        text_button_det.setOnClickListener {
            intent.extras?.let{
                val book = it.getSerializable("book") as datosLibro
                alter_text.text = getString(R.string.lorem_Ipsum_Det,book.id)
            }
        }

    }
}