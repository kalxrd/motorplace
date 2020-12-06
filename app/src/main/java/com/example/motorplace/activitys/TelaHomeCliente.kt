package com.example.motorplace.activitys

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.motorplace.R
import com.example.motorplace.fragments.cliente.*
import com.example.motorplace.model.Carro
import com.example.motorplace.model.Usuario
import com.example.motorplace.util.carroAtual
import com.example.motorplace.util.userAtual
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_tela_home_cliente.*
import kotlinx.android.synthetic.main.toolbar.*


class TelaHomeCliente : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_home_cliente)

        supportActionBar!!.hide()



        inicializar()
        carregarDados()
        //seta o primeiro fragmente ao abrir a activity
        trocarFragment(HomeFragment())

        //habilita a navegação do botton navigation
        habilitarNavegacao()


        button_carrinho.setOnClickListener {
            startActivity(Intent(this,CarrinhoActivity::class.java))
        }
    }
    private fun inicializar(){
        userAtual = Usuario()
        carroAtual = Carro()
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    private fun carregarDados(){
        val user =auth.currentUser
        val usuario = database.child("usuarios").child(auth.uid!!)
        val carro = database.child("carros").child(auth.uid!!)

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

            carro.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val c = dataSnapshot.getValue(Carro::class.java)!!

                    carroAtual.idUsuario = c.idUsuario
                    carroAtual.placa = c.placa
                    carroAtual.modelo = c.modelo
                    carroAtual.marca = c.marca
                    carroAtual.ano = c.ano
                    carroAtual.cor = c.cor
                    carroAtual.tamanho = c.tamanho
                }

            })
        }
    }

    fun habilitarNavegacao(){
        //metodo de click do bottom
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    titulo_toolbar.text = "Feed"
                    trocarFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_agenda -> {
                    titulo_toolbar.text = "Agenda"
                    trocarFragment(AgendaFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_produtos -> {
                    titulo_toolbar.text = "Loja"
                    trocarFragment(ProdutosFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_servicos -> {
                    titulo_toolbar.text = "Serviços"
                    trocarFragment(ServicosFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_perfil -> {
                    titulo_toolbar.text = "Perfil"
                    trocarFragment(PerfilFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_toolbar, menu)
//        return true
//    }

    fun trocarFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment).commit()
    }
}