package com.example.motorplace.fragments.cliente

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private var servicosSolicitados = arrayListOf<ServicosSolicitados>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_agenda, container, false)



        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")

        recyclerViewServicos = view.findViewById(R.id.recycler_agenda)
        recyclerViewServicos.layoutManager = LinearLayoutManager(view.context)
        recyclerViewServicos.hasFixedSize()

        adapterAgenda = AgendaAdapter(view.context!!,servicos,servicosSolicitados,servicosRecuperados)

        recyclerViewServicos.adapter = adapterAgenda




        //preenche os dados do spinner
        val spinnerTamanho: Spinner = view.findViewById(R.id.spinner_filtro_agenda)
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

        //recupera dados
        //recuperarServico("Todas as categorias")
        return view
    }


    private fun recuperarServico(categoria : String){
        if (categoria.equals("Todas as categorias")){
            servicosRecuperados.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val u = d.getValue(Servico::class.java)
                        //Toast.makeText(context,"${d.key}",Toast.LENGTH_SHORT).show()

                        servicosRecuperados.child(d.key.toString()).child("servicosSolicitados").addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                servicos.clear()
                                servicosSolicitados.clear()
                                for (d in dataSnapshot.children){
                                    val u = d.getValue(ServicosSolicitados::class.java)
                                    if(u!!.idCliente.equals(carroAtual.idUsuario)){
                                        FirebaseDatabase.getInstance().reference.child("servicos").child(u!!.idServico).addValueEventListener(object : ValueEventListener{
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                var loren = dataSnapshot.getValue(Servico::class.java)!!


                                                servicos.add(loren!!)
                                                servicosSolicitados.add(u!!)

                                                Collections.reverse(servicos)
                                                Collections.reverse(servicosSolicitados)
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

            })

        }else{ //fim do if

            servicosRecuperados.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val u = d.getValue(Servico::class.java)
                        //Toast.makeText(context,"${d.key}",Toast.LENGTH_SHORT).show()

                        servicosRecuperados.child(d.key.toString()).child("servicosSolicitados").addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                servicos.clear()
                                servicosSolicitados.clear()
                                for (d in dataSnapshot.children){
                                    val u = d.getValue(ServicosSolicitados::class.java)
                                    if(u!!.idCliente.equals(carroAtual.idUsuario)){
                                        FirebaseDatabase.getInstance().reference.child("servicos").child(u!!.idServico).addValueEventListener(object : ValueEventListener{
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                var loren = dataSnapshot.getValue(Servico::class.java)!!

                                                if (loren!!.categoria.equals(categoria)){
                                                    servicos.add(loren!!)
                                                    servicosSolicitados.add(u!!)
                                                }


                                                Collections.reverse(servicos)
                                                Collections.reverse(servicosSolicitados)
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

            })

        }

    }
}