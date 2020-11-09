package com.example.motorplace.fragments.cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.adapter.ServicosAdapter
import com.example.motorplace.model.Servico
import com.google.firebase.database.*
import java.util.*


class ServicosFragment : Fragment() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var servicos = arrayListOf<Servico>()
    private lateinit var adapterServico: ServicosAdapter
    private lateinit var servicosRecuperados : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_servicos, container, false)

        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")

        recyclerViewServicos = view.findViewById(R.id.recycler_servicos_cliente)
        recyclerViewServicos.layoutManager = LinearLayoutManager(view.context)
        recyclerViewServicos.hasFixedSize()

        adapterServico = ServicosAdapter(view.context!!,servicos)

        recyclerViewServicos.adapter = adapterServico

        //recupera dados
        recuperarServico()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*  codigo para futura implementação
        val viewPager: ViewPager = view!!.findViewById(R.id.viewPager)
        val viewPagerTab: SmartTabLayout = view!!.findViewById(R.id.viewPagerTab)

        //Configurar abas
        val adapter = FragmentPagerItemAdapter(
            fragmentManager,
            FragmentPagerItems.with(context)
                .add("Revisões", RevisoesFragment::class.java)
                .add("Pintura e Funilaria", PinturaFragment::class.java)
                .create()
        )
        viewPager.adapter = adapter
        viewPagerTab.setViewPager(viewPager)


         */
    }

    private fun recuperarServico(){
        servicosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                servicos.clear()
                for (d in dataSnapshot.children){
                    val u = d.getValue(Servico::class.java)
                    servicos.add(u!!)
                }

                Collections.reverse(servicos)
                adapterServico.notifyDataSetChanged()
            }

        })

    }
}