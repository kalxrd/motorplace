package com.example.motorplace.fragments.cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.adapter.AgendaAdapter
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_agenda.view.*
import java.util.*


class AgendaFragment : Fragment() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var servicos = arrayListOf<Servico>()
    private lateinit var adapterAgenda: AgendaAdapter
    private lateinit var servicosRecuperados : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_agenda, container, false)


        //criacao do spinner
        val spinnerFiltro = resources.getStringArray(R.array.filtro) //recupera o array do string.xml
        view.spinner_filtro.setAdapter(ArrayAdapter(view.context,
            R.layout.support_simple_spinner_dropdown_item, spinnerFiltro)) //seta o adapter no spinner

        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicosSolicitados")

        recyclerViewServicos = view.findViewById(R.id.recycler_agenda)
        recyclerViewServicos.layoutManager = LinearLayoutManager(view.context)
        recyclerViewServicos.hasFixedSize()

        adapterAgenda = AgendaAdapter(view.context!!,servicos)

        recyclerViewServicos.adapter = adapterAgenda

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
                    val u = d.getValue(ServicosSolicitados::class.java)
                    if(u!!.idCliente.equals(carroAtual.idUsuario)){
                        FirebaseDatabase.getInstance().reference.child("servicos").child(u!!.idServico).addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                               var loren = dataSnapshot.getValue(Servico::class.java)!!


                                servicos.add(loren!!)

                                Collections.reverse(servicos)
                                adapterAgenda.notifyDataSetChanged()
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