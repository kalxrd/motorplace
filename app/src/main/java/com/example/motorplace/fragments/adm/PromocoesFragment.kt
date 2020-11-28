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
import com.example.motorplace.adapter.ServicosPromocoesAdm
import com.example.motorplace.model.Servico
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_promocoes.view.*
import java.util.*

class PromocoesFragment : Fragment() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var servicos = arrayListOf<Servico>()
    private lateinit var adapterServico: ServicosPromocoesAdm
    private lateinit var servicosRecuperados : DatabaseReference
    private lateinit var servicosExcluidos : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_promocoes, container, false)


        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")


        recyclerViewServicos = view.findViewById(R.id.recycler_promocoes_cliente)
        recyclerViewServicos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewServicos.hasFixedSize()

        adapterServico = ServicosPromocoesAdm(view.context!!,servicos,servicosRecuperados)

        recyclerViewServicos.adapter = adapterServico

        //recupera dados
        recuperarServico("Todas as categorias")

        //preenche os dados do spinner
        val spinnerTamanho: Spinner = view.findViewById(R.id.spinner_filtro_promocoes_adm)
        ArrayAdapter.createFromResource(
            view.context!!, R.array.filtro, android.R.layout.simple_spinner_item
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
                recuperarServico( spinnerTamanho.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
         

        return view
    }


    private fun recuperarServico(categoria : String){
        if (categoria.equals("Todas as categorias")){
            servicosRecuperados.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    servicos.clear()
                    for (d in dataSnapshot.children){
                        var u = d.getValue(Servico::class.java)
                        if(!u!!.prazo.isEmpty()){
                            servicos.add(u!!)
                        }

                    }

                    Collections.reverse(servicos)
                    adapterServico.notifyDataSetChanged()
                }

            })

        }else{
            servicosRecuperados.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    servicos.clear()
                    for (d in dataSnapshot.children){
                        var u = d.getValue(Servico::class.java)
                        if(!u!!.prazo.isEmpty()){
                            if (u.categoria.equals(categoria)){
                                servicos.add(u!!)
                            }
                        }

                    }

                    Collections.reverse(servicos)
                    adapterServico.notifyDataSetChanged()
                }

            })

        }

    }
}