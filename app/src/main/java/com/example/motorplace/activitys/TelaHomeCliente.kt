package com.example.motorplace.activitys



import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.motorplace.R
import com.example.motorplace.fragments.AgendaFragment
import com.example.motorplace.fragments.HomeFragment
import com.example.motorplace.fragments.PerfilFragment
import com.example.motorplace.fragments.ServicosFragment
import kotlinx.android.synthetic.main.activity_tela_home_cliente.*



class TelaHomeCliente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_home_cliente)

        supportActionBar!!.title = "Feed"
        //seta o primeiro fragmente ao abrir a activity
        trocarFragment(HomeFragment())

        //habilita a navegação do botton navigation
        habilitarNavegacao()
    }

    fun habilitarNavegacao(){
        //metodo de click do bottom
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    supportActionBar!!.title = "Feed"
                    trocarFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_agenda -> {
                    supportActionBar!!.title = "Agenda"
                    trocarFragment(AgendaFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_servicos -> {
                    supportActionBar!!.title = "Revisões"
                    trocarFragment(ServicosFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_perfil -> {
                    supportActionBar!!.title = "Perfil"
                    trocarFragment(PerfilFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    fun trocarFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment).commit()
    }
}