package com.example.motorplace.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.activitys.EditarProdutoAdmActivity
import com.example.motorplace.model.Produto
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso

class ProdutosAdapter (private val context: Context, private val listProdutos: ArrayList<Produto>, private val produtosRecuperados: DatabaseReference) : RecyclerView.Adapter<ProdutosAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_servicos,viewGroup,false)
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

    }

    inner  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo : TextView
        var valor : TextView
        var foto : ImageView

        init {
            titulo = itemView.findViewById(R.id.txt_titulo_cliente_list)
            valor = itemView.findViewById(R.id.txt_valor_cliente)
            foto = itemView.findViewById(R.id.image_servicos_cliente)

        }
    }

}