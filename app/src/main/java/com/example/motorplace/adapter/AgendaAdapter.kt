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

class AgendaAdapter (private val context: Context, private val listServicos: ArrayList<Servico>) : RecyclerView.Adapter<AgendaAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_agenda,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listServicos.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val servico = listServicos.get(i)
        myViewHolder.titulo.text = servico.titulo
        myViewHolder.valor.text = servico.valor
        myViewHolder.descricao.text = servico.descricao


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
        var descricao : TextView
        var valor : TextView
        var foto : ImageView

        init {
            titulo = itemView.findViewById(R.id.titulo_agenda)
            descricao =  itemView.findViewById(R.id.descricao_agenda)
            valor = itemView.findViewById(R.id.agenda_valor)
            foto = itemView.findViewById(R.id.image_agenda)

        }
    }
}