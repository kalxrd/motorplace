package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.motorplace.R
import com.example.motorplace.adapter.ServicosAdapter
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_favoritos_servicos.*
import java.util.*

class FavoritosServicosActivity : AppCompatActivity() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var servicos = arrayListOf<Servico>()
    private lateinit var adapterServico: ServicosAdapter
    private lateinit var servicosRecuperados : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos_servicos)

        supportActionBar!!.title = "Serviços Favoritos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")

        recyclerViewServicos = findViewById(R.id.recycler_favoritos_servicos)
        recyclerViewServicos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewServicos.hasFixedSize()

        adapterServico = ServicosAdapter(this,servicos)

        recyclerViewServicos.adapter = adapterServico

        //recupera dados
        recuperarServico()
    }
    private fun recuperarServico(){
        servicosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    val u = d.getValue(Servico::class.java)

                    servicosRecuperados.child(d.key.toString()).child("favoritos").addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            servicos.clear()
                            for (d in dataSnapshot.children){
                                if(d.key.toString().equals(carroAtual.idUsuario)){
                                    if(d.exists()){
                                        linear_fav_servicos.visibility = View.GONE
                                    }else{
                                        linear_fav_servicos.visibility = View.VISIBLE
                                    }
                                    FirebaseDatabase.getInstance().reference.child("servicos").child(u!!.id).addValueEventListener(object : ValueEventListener{
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            var loren = dataSnapshot.getValue(Servico::class.java)!!


                                            servicos.add(loren!!)

//                                            if (servicos.isEmpty()){
//                                                linear_fav_servicos.visibility = View.VISIBLE
//                                            }else{
//                                                linear_fav_servicos.visibility = View.GONE
//                                            }

                                            Collections.reverse(servicos)
                                            adapterServico.notifyDataSetChanged()
                                        }

                                        override fun onCancelled(p0: DatabaseError) {
                                        }

                                    })
                                }
                            }

                        }
                    })




                }
            }

        })

    }
}