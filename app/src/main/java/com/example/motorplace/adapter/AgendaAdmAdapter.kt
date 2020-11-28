package com.example.motorplace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados

class AgendaAdmAdapter (private val context: Context, private val listServicos: ArrayList<ServicosSolicitados>,private val listServicos2: ArrayList<Servico>) : RecyclerView.Adapter<AgendaAdmAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_servicos_calendario,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listServicos.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val servico = listServicos.get(i)
        val servico2 = listServicos2.get(i)
        myViewHolder.nomeUsuario.text = "Cliente: " +servico.nomeCliente
        myViewHolder.titulo.text = "Serviço: " + servico2.titulo
        myViewHolder.hora.text = "Hora de Solicitação: " + servico.hora+"h"
        myViewHolder.dia.text = servico.diaSemana + ", " + servico.data

    }

    inner  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomeUsuario : TextView
        var titulo : TextView
        var hora : TextView
        var dia : TextView

        init {
            nomeUsuario = itemView.findViewById(R.id.calendar_nome)
            titulo = itemView.findViewById(R.id.list_titulo_servico)
            hora = itemView.findViewById(R.id.list_hora)
            dia = itemView.findViewById(R.id.list_dia)

        }
    }
}