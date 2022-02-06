package mx.kodemia.bookodemia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_cardview_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val listFragment = ListFragment()
        val homeFragment = HomeFragment()
        val userFragment = UserFragment()

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_list -> {
                    setCurrentFragment(listFragment)
                    true
                }
                R.id.nav_home -> {
                    setCurrentFragment(homeFragment)
                    true
                }
                R.id.nav_user -> {
                    setCurrentFragment(userFragment)
                    true
                }
                else -> false
            }
        }
    }

    fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.ContainerView, fragment)
            commit()
        }
    }
}