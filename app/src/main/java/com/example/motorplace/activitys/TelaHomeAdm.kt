package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.motorplace.R
import com.example.motorplace.fragments.AgendaFragment
import com.example.motorplace.fragments.HomeFragment
import com.example.motorplace.fragments.PerfilFragment
import com.example.motorplace.fragments.ServicosFragment

class TelaHomeAdm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_home_cliente)
        supportActionBar!!.hide()

        //Inicializa o Bottom Navigation junto com o evento de click
        val navigation = findViewById(R.id.bottomNavigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Adiciona o primeiro fragment na tela que é o Home
        openFragment(HomeFragment())


    }

    //Método de evento das opções do Bottom Navigation
    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                //Switch que verifica com o item selecionado
                when (item.itemId) {
                    R.id.navigation_home -> {
                        //Se caso tiver na opção Home, ele vai instanciar o fragment e jogar na tela
                        openFragment(HomeFragment())
                        return true
                    }
                    //Se caso tiver na opção Agenda, ele vai instanciar o fragment e jogar na tela
                    R.id.navigation_agenda -> {
                        openFragment(AgendaFragment())
                        return true
                    }
                    //Se caso tiver na opção Serviços, ele vai instanciar o fragment e jogar na tela
                    R.id.navigation_servicos -> {
                        openFragment(ServicosFragment())
                        return true
                    }
                    //Se caso tiver na opção Perfil, ele vai instanciar o fragment e jogar na tela
                    R.id.navigation_perfil -> {
                        openFragment(PerfilFragment())
                        return true
                    }
                }
                return false
            }

        }

    //Método que instancia o Fragment
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
