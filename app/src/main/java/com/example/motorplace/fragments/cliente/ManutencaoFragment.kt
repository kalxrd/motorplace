package com.example.motorplace.fragments.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.motorplace.R
import com.example.motorplace.adapter.ProdutosAdapter
import com.example.motorplace.model.Produto
import com.google.firebase.database.*
import java.util.*


class ManutencaoFragment : Fragment() {
    private lateinit var recyclerViewProdutos: RecyclerView
    private var produtos = arrayListOf<Produto>()
    private lateinit var adapterProduto: ProdutosAdapter
    private lateinit var produtosRecuperados : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_manutencao, container, false)

        produtosRecuperados =  FirebaseDatabase.getInstance().reference.child("produtos")


        recyclerViewProdutos = view.findViewById(R.id.recycler_produtos_manutencao)
        recyclerViewProdutos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewProdutos.hasFixedSize()

        adapterProduto = ProdutosAdapter(view.context!!,produtos,produtosRecuperados)

        recyclerViewProdutos.adapter = adapterProduto

        //recupera dados
        recuperarProduto()

        return view
    }

    private fun recuperarProduto(){
        produtosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                produtos.clear()
                for (d in dataSnapshot.children){
                    var u = d.getValue(Produto::class.java)
                    if(u!!.categoria.equals("Manutenção")){
                        produtos.add(u!!)
                    }
                }

                Collections.reverse(produtos)
                adapterProduto.notifyDataSetChanged()
            }

        })

    }

}