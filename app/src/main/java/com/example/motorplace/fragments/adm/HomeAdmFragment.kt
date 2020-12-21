package com.example.motorplace.fragments.adm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.motorplace.R
import com.example.motorplace.adapter.AgendaAdapter
import com.example.motorplace.model.Compras
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home_adm.*
import java.text.DecimalFormat
import java.util.*

class HomeAdmFragment : Fragment() {
    private lateinit var progressServicos: ProgressBar
    private lateinit var progressProdutos: ProgressBar
    private lateinit var servicosRecuperados : DatabaseReference
    private var servicos = arrayListOf<Servico>()
    private var servicosSolicitados = arrayListOf<ServicosSolicitados>()

    private var soma = 0.00
    private var somaCusto = 0.00
    private lateinit var valorServicos: TextView
    private lateinit var custoServicos: TextView
    private lateinit var lucroServicos: TextView

    private lateinit var produtosRecuperados : DatabaseReference
    private var produtos = arrayListOf<Compras>()
    private var somaProduto = 0.00
    private var somaCustoProduto = 0.00
    private lateinit var valorProduto: TextView
    private lateinit var custoProdutos: TextView
    private lateinit var lucroProdutos: TextView

    private lateinit var porcentagemProduto : TextView
    private lateinit var porcentagemServico : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_home_adm, container, false)

        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")
        produtosRecuperados =  FirebaseDatabase.getInstance().reference.child("compras")

        porcentagemProduto = view.findViewById(R.id.porcentagem_produto)
        porcentagemServico = view.findViewById(R.id.porcentagem)


        progressServicos = view.findViewById(R.id.progressCompleted)
        progressProdutos = view.findViewById(R.id.progressProdutos)

        valorServicos = view.findViewById(R.id.valor_servicos)
        custoServicos = view.findViewById(R.id.custo_servico)
        lucroServicos = view.findViewById(R.id.lucro)

        valorProduto = view.findViewById(R.id.valor_produtos)
        custoProdutos = view.findViewById(R.id.custo_produtos)
        lucroProdutos = view.findViewById(R.id.lucro_produtos)

        recuperarServico()
        recuperarProduto()

        return view
    }

    fun formataDados(dado: String): String? {
        var newDado = dado
        newDado = dado.replace(".", "")
        newDado = newDado.replace(",", ".")
        return newDado
    }

    fun formtarDecimal(saldo: Double): String? {
        val df = DecimalFormat("0.00")
        df.setMaximumFractionDigits(2)
        return df.format(saldo)
    }

    private fun recuperarProduto(){
        produtosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot){
                somaProduto = 0.00
                somaCustoProduto = 0.00
                valorProduto.text = somaProduto.toString()
                custoProdutos.text = somaCustoProduto.toString()
                lucroProdutos.text = (somaProduto - somaCustoProduto).toString()
                produtos.clear()
                for (d in dataSnapshot.children){
                    val u = d.getValue(Compras::class.java)
                    produtos.add(u!!)

                    somaProduto += formataDados(u.valor.substring(3))!!.toDouble()
                    somaCustoProduto += formataDados(u.custo.substring(3))!!.toDouble()
                    valorProduto.text = "R$ "+formtarDecimal(somaProduto)
                    custoProdutos.text = "R$ "+formtarDecimal(somaCustoProduto)
                    lucroProdutos.text = "R$ "+formtarDecimal(somaProduto - somaCustoProduto).toString()

                    var perce = getPercentage(somaProduto - somaCustoProduto,somaProduto);

                    porcentagemProduto.text = perce.toString().substring(0,5)+"%"

                    progressProdutos.setProgress(Math.round(perce));
                    Collections.reverse(produtos)
                }
            }

        })
    }

    private fun recuperarServico(){
        servicosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot){
                soma = 0.00
                somaCusto = 0.00
                valorServicos.text = soma.toString()
                custoServicos.text = somaCusto.toString()
                lucroServicos.text = (soma - somaCusto).toString()
                for (d in dataSnapshot.children){
                    val u = d.getValue(Servico::class.java)
                    //Toast.makeText(context,"${d.key}",Toast.LENGTH_SHORT).show()

                    servicosRecuperados.child(d.key.toString()).child("servicosSolicitados").addValueEventListener(object :
                        ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            servicos.clear()
                            servicosSolicitados.clear()
                            for (d in dataSnapshot.children){
                                val u = d.getValue(ServicosSolicitados::class.java)

                                FirebaseDatabase.getInstance().reference.child("servicos").child(u!!.idServico).addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        var loren = dataSnapshot.getValue(Servico::class.java)!!


                                        if (!loren.custo.isEmpty()){
                                            servicos.add(loren!!)
                                            servicosSolicitados.add(u!!)

                                            soma += formataDados(loren.valor.substring(3))!!.toDouble()
                                            somaCusto += formataDados(loren.custo.substring(3))!!.toDouble()
                                            valorServicos.text = "R$ "+formtarDecimal(soma)
                                            custoServicos.text = "R$ "+formtarDecimal(somaCusto)
                                            lucroServicos.text = "R$ "+formtarDecimal(soma - somaCusto)

                                            var perce = getPercentage(soma - somaCusto,soma);


                                            porcentagemServico.text = perce.toString().substring(0,5)+"%"

                                            progressServicos.setProgress(Math.round(perce));
                                            Collections.reverse(servicos)
                                            Collections.reverse(servicosSolicitados)
                                        }
                                        //adapterAgenda.notifyDataSetChanged()
                                    }
                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                })

                            }
                        }
                    })




                }
            }

        })
    }

    fun  getPercentage(value : Double,total: Double) : Float{
        if(value != 0.00 && total != 0.00){
            var percentage = (value * 100) / total;
            return percentage.toFloat()
        }
        return 0F;
    }
}