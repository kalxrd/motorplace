package com.example.motorplace.activitys

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.motorplace.R
import com.example.motorplace.fragments.AgendaFragment
import com.example.motorplace.fragments.HomeFragment
import com.example.motorplace.fragments.PerfilFragment
import com.example.motorplace.fragments.ServicosFragment
import com.example.motorplace.model.Usuario
import com.example.motorplace.util.userAtual
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tela_home_cliente.*



class TelaHomeCliente : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_home_cliente)

        supportActionBar!!.title = "Feed"

        inicializar()
        carregarDados()
        //seta o primeiro fragmente ao abrir a activity
        trocarFragment(HomeFragment())


        //habilita a navegação do botton navigation
        habilitarNavegacao()
    }
    private fun inicializar(){
        userAtual = Usuario()
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    private fun carregarDados(){
        val user =auth.currentUser
        val usuario = database.child("usuarios").child(auth.uid!!)

        user?.let{
            usuario.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val u = dataSnapshot.getValue(Usuario::class.java)!!

                    userAtual.nome = u.nome
                    userAtual.email = u.email
                    userAtual.cpf = u.cpf
                    userAtual.telefone = u.telefone
                    userAtual.dataNasc = u.dataNasc
                    userAtual.foto = u.foto
                }

            })
        }
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