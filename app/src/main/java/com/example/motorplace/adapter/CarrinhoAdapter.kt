package com.example.motorplace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.model.Produto
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class CarrinhoAdapter (private val context: Context, private val listProdutos: ArrayList<Produto>, private val produtosRecuperados: DatabaseReference) : RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_carrinho,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listProdutos.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val produto = listProdutos.get(i)
        myViewHolder.titulo.text = produto.titulo
        myViewHolder.valor.text = produto.valor



        //pega a primeira imagem da lista
        Picasso.get()
            .load(produto?.foto)
            .into(myViewHolder.foto)

        myViewHolder.excluir.setOnClickListener {
            produtosRecuperados.child(produto.id).removeValue()
        }


    }



    inner  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo : TextView
        var valor : TextView
        var foto : ImageView
        var excluir : ImageButton

        init {
            titulo = itemView.findViewById(R.id.titulo_carrinho)
            valor = itemView.findViewById(R.id.preco_carrinho)
            foto = itemView.findViewById(R.id.image_carrinho)
            excluir = itemView.findViewById(R.id.excluir_carrinho)

        }
    }

}