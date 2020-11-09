package com.example.motorplace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.model.Servico
import com.squareup.picasso.Picasso


class ServicosAdapter (private val context: Context, private val listServicos: ArrayList<Servico>) : RecyclerView.Adapter<ServicosAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_servicos,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listServicos.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val servico = listServicos.get(i)
        myViewHolder.titulo.text = servico.titulo
        myViewHolder.valor.text = servico.valor


        //metodo de click
        myViewHolder.itemView.setOnClickListener {
        }

        //pega a primeira imagem da lista
        Picasso.get()
            .load(servico?.foto)
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