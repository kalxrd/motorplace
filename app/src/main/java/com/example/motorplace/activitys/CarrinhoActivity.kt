package com.example.motorplace.activitys

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.adapter.CarrinhoAdapter
import com.example.motorplace.model.Produto
import com.example.motorplace.util.MoneyTextWatcher
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_carrinho.*
import java.text.DecimalFormat
import java.util.*

class CarrinhoActivity : AppCompatActivity() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var produtos = arrayListOf<Produto>()
    private lateinit var adapterProduto: CarrinhoAdapter
    private lateinit var produtosRecuperados : DatabaseReference
    private lateinit var carrinhoRecuperados : DatabaseReference
    private var soma = 0.00
    private lateinit var valor: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)

        supportActionBar!!.title = "Carrinho"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        valor = findViewById(R.id.valor_total)
        produtosRecuperados =  FirebaseDatabase.getInstance().reference.child("produtos")
        carrinhoRecuperados =  FirebaseDatabase.getInstance().reference.child("usuarios").child(
            carroAtual.idUsuario
        ).child("carrinho")

        recyclerViewServicos = findViewById(R.id.recycler_carrinho)
        recyclerViewServicos.layoutManager =  LinearLayoutManager(this)
        recyclerViewServicos.hasFixedSize()

        adapterProduto = CarrinhoAdapter(this, produtos, carrinhoRecuperados)

        recyclerViewServicos.adapter = adapterProduto

        //recupera dados
        recuperarProduto()

        var mLocale = Locale("pt", "BR")
        valor_total.addTextChangedListener(MoneyTextWatcher(valor_total, mLocale))

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
    private fun recuperarProduto() {
        carrinhoRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                produtos.clear()
                soma = 0.00
                valor.text = soma.toString()
                for (d in dataSnapshot.children) {
                    produtosRecuperados.child(d.key.toString()).addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val prod = dataSnapshot.getValue(Produto::class.java)!!

                            soma += formataDados(prod.valor.substring(3))!!.toDouble()
                            valor.text = formtarDecimal(soma)
                            produtos.add(prod)

                            Collections.reverse(produtos)
                            adapterProduto.notifyDataSetChanged()
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }

                    })
                }
            }
        })
    }
}