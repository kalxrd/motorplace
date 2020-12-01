package com.example.motorplace.fragments.adm

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.motorplace.R
import com.example.motorplace.adapter.ProdutosAdmAdapter
import com.example.motorplace.model.Produto
import com.example.motorplace.model.Servico
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


        produtosRecuperados =  FirebaseDatabase.getInstance().reference.child("produtos")


        recyclerViewProdutos = view.findViewById(R.id.recyclerViewProdutos)
        recyclerViewProdutos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewProdutos.hasFixedSize()

        adapterProduto = ProdutosAdmAdapter(view.context!!,produtos,produtosRecuperados)

        recyclerViewProdutos.adapter = adapterProduto

        //recupera dados
        recuperarProduto("Todas as categorias")


        //preenche os dados do spinner
        val spinnerTamanho: Spinner = view.findViewById(R.id.spinner_filtro_produtos_adm)
        ArrayAdapter.createFromResource(
            view.context!!, R.array.filtro_produto, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerTamanho.adapter = adapter
        }

        //verifica o spinner selecionado
        spinnerTamanho.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                recuperarProduto( spinnerTamanho.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        return view
    }


    private fun recuperarProduto(categoria : String){
        if(categoria.equals("Todas as categorias")){
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

        }else{
            produtosRecuperados.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    produtos.clear()
                    for (d in dataSnapshot.children){
                        var u = d.getValue(Produto::class.java)
                        if (u!!.categoria.equals(categoria)){
                            produtos.add(u!!)
                        }
                    }
                    Collections.reverse(produtos)
                    adapterProduto.notifyDataSetChanged()
                }

            })
        }
    }
}