package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.motorplace.R
import com.example.motorplace.adapter.ProdutosAdapter
import com.example.motorplace.adapter.ServicosAdapter
import com.example.motorplace.fragments.cliente.ProdutosFragment
import com.example.motorplace.model.Produto
import com.example.motorplace.model.Servico
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_favoritos_produtos.*
import java.util.*

class FavoritosProdutosActivity : AppCompatActivity() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var produtos = arrayListOf<Produto>()
    private lateinit var adapterProduto: ProdutosAdapter
    private lateinit var produtosRecuperados : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos_produtos)

        supportActionBar!!.title = "Produtos Favoritos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        produtosRecuperados =  FirebaseDatabase.getInstance().reference.child("produtos")

        recyclerViewServicos = findViewById(R.id.recycler_favoritos_produtos)
        recyclerViewServicos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewServicos.hasFixedSize()

        adapterProduto = ProdutosAdapter(this,produtos,produtosRecuperados)

        recyclerViewServicos.adapter = adapterProduto

        //recupera dados
        recuperarServico()
    }
    private fun recuperarServico(){
        produtosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    val u = d.getValue(Produto::class.java)

                    produtosRecuperados.child(d.key.toString()).child("favoritos").addValueEventListener(object :
                        ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            produtos.clear()
                            for (d in dataSnapshot.children){
                                if(d.key.toString().equals(carroAtual.idUsuario)){
                                    if(d.exists()){
                                        linear_fav_produtos.visibility = View.GONE
                                    }else{
                                        linear_fav_produtos.visibility = View.VISIBLE
                                    }
                                    FirebaseDatabase.getInstance().reference.child("produtos").child(u!!.id).addValueEventListener(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            var loren = dataSnapshot.getValue(Produto::class.java)!!


                                            produtos.add(loren!!)


                                            Collections.reverse(produtos)
                                            adapterProduto.notifyDataSetChanged()
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