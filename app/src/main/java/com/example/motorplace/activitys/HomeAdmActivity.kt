package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.motorplace.R
import com.example.motorplace.fragments.adm.*
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.util.datasSolicitadas
import com.example.motorplace.util.servicosGlobal
import com.example.motorplace.util.servicosSolicitadosGlobal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home_adm.*
import java.util.*

class HomeAdmActivity : AppCompatActivity() {
    private  var tela = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_adm)

        supportActionBar!!.title = "Feed"

        carregarDados()
        //verifica se ta na tela produtos ou serviços
        fab_add.setOnClickListener {
            if(tela == 1){
                startActivity(Intent(this,CadastroServicoActivity::class.java))
            }else if (tela == 2){
                startActivity(Intent(this,CadastrarProdutoActivity::class.java))
            }

        }
        trocarFragment(HomeAdmFragment())
        //habilita a navegação do botton navigation
        habilitarNavegacao()
    }

    fun habilitarNavegacao(){
        //metodo de click do bottom
        bottom_navigation_adm.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home_adm -> {
                    supportActionBar!!.title = "Feed"
                    fab_add.visibility = View.GONE
                    trocarFragment(HomeAdmFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_agenda_adm -> {
                    supportActionBar!!.title = "Agenda"
                    fab_add.visibility = View.GONE
                    trocarFragment(AgendaAdmFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_produtos_adm -> {
                    supportActionBar!!.title = "Produtos"
                    tela = 2
                    fab_add.visibility = View.VISIBLE
                    trocarFragment(ProdutosAdmFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_servicos_adm -> {
                    supportActionBar!!.title = "Serviços"
                    tela = 1
                    fab_add.visibility = View.VISIBLE
                    trocarFragment(ServicosAdmFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_perfil_adm -> {
                    supportActionBar!!.title = "Perfil"
                    fab_add.visibility = View.GONE
                    trocarFragment(PerfilAdmFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
    fun trocarFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_adm, fragment).commit()
    }

    fun carregarDados(){
        var  servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")
        datasSolicitadas = arrayListOf<String>()
        servicosGlobal =  arrayListOf<Servico>()
        servicosSolicitadosGlobal = arrayListOf<ServicosSolicitados>()


        servicosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    servicosRecuperados.child(d.key.toString()).child("servicosSolicitados").addValueEventListener(object :
                        ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (d in dataSnapshot.children){
                                val u = d.getValue(ServicosSolicitados::class.java)


                                FirebaseDatabase.getInstance().reference.child("servicos").child(u!!.idServico).addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        var loren = dataSnapshot.getValue(Servico::class.java)!!

                                        servicosGlobal.add(loren!!)
                                        servicosSolicitadosGlobal.add(u!!)
                                        datasSolicitadas.add(u!!.data)

                                        Collections.reverse(servicosGlobal)
                                        Collections.reverse(servicosSolicitadosGlobal)
                                        Collections.reverse(datasSolicitadas)

                                    }

                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                })

                            }
                        }
                    })
                }
            }
        })
    }
}