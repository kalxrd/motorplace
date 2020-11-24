package com.example.motorplace.fragments.adm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.motorplace.R
import com.example.motorplace.adapter.ServicosPromocoesAdm
import com.example.motorplace.model.Promocao
import com.google.firebase.database.*
import java.util.*

class PromocoesFragment : Fragment() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var servicos = arrayListOf<Promocao>()
    private lateinit var adapterServico: ServicosPromocoesAdm
    private lateinit var servicosRecuperados : DatabaseReference
    private lateinit var servicosExcluidos : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_promocoes, container, false)

        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("promocoes")


        recyclerViewServicos = view.findViewById(R.id.recycler_promocoes_cliente)
        recyclerViewServicos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewServicos.hasFixedSize()

        adapterServico = ServicosPromocoesAdm(view.context!!,servicos,servicosRecuperados)

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
                    var u = d.getValue(Promocao::class.java)
                    servicos.add(u!!)


                }

                Collections.reverse(servicos)
                adapterServico.notifyDataSetChanged()
            }

        })

    }
}