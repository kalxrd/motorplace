package com.example.motorplace.fragments.adm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.adapter.ServicosAdmAdapter
import com.example.motorplace.model.Servico
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_servicos_adm.view.*
import java.util.*


class ServicosAdmFragment : Fragment(){
    private lateinit var recyclerViewServicos: RecyclerView
    private var servicos = arrayListOf<Servico>()
    private lateinit var adapterServico: ServicosAdmAdapter
    private lateinit var servicosRecuperados : DatabaseReference
    private lateinit var servicosExcluidos : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_servicos_adm, container, false)

        //criacao do spinner
        val spinnerFiltro = resources.getStringArray(R.array.filtro) //recupera o array do string.xml
        view.spinner_filtro_adm.setAdapter(
            ArrayAdapter(view.context,R.layout.support_simple_spinner_dropdown_item, spinnerFiltro)
        ) //seta o adapter no spinner

        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")
        servicosExcluidos =  FirebaseDatabase.getInstance().reference.child("servicosSolicitados")

        recyclerViewServicos = view.findViewById(R.id.recyclerView)
        recyclerViewServicos.layoutManager = LinearLayoutManager(view.context)
        recyclerViewServicos.hasFixedSize()

        adapterServico = ServicosAdmAdapter(view.context!!,servicos,servicosRecuperados,servicosExcluidos)

        recyclerViewServicos.adapter = adapterServico

        //recupera dados
        recuperarServico()

        return view
    }


    private fun recuperarServico(){
        servicosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                servicos.clear()
                for (d in dataSnapshot.children){
                    var u = d.getValue(Servico::class.java)
                    servicos.add(u!!)
                }

                Collections.reverse(servicos)
                adapterServico.notifyDataSetChanged()
            }

        })

    }
}