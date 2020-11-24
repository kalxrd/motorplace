package com.example.motorplace.fragments.adm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.motorplace.R
import com.example.motorplace.adapter.ProdutosAdmAdapter
import com.example.motorplace.model.Produto
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_produtos_adm.view.*
import java.util.*


class ProdutosAdmFragment : Fragment() {
    private lateinit var recyclerViewProdutos: RecyclerView
    private var produtos = arrayListOf<Produto>()
    private lateinit var adapterProduto: ProdutosAdmAdapter
    private lateinit var produtosRecuperados : DatabaseReference
   

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_produtos_adm, container, false)

        //criacao do spinner
        val spinnerFiltro = resources.getStringArray(R.array.filtro) //recupera o array do string.xml
        view.spinner_filtro_produtos_adm.setAdapter(
            ArrayAdapter(view.context,R.layout.support_simple_spinner_dropdown_item, spinnerFiltro)
        ) //seta o adapter no spinner

        produtosRecuperados =  FirebaseDatabase.getInstance().reference.child("produtos")


        recyclerViewProdutos = view.findViewById(R.id.recyclerViewProdutos)
        recyclerViewProdutos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewProdutos.hasFixedSize()

        adapterProduto = ProdutosAdmAdapter(view.context!!,produtos,produtosRecuperados)

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
                    produtos.add(u!!)
                }

                Collections.reverse(produtos)
                adapterProduto.notifyDataSetChanged()
            }

        })

    }
}