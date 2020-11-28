package com.example.motorplace.adapter

import android.app.AlertDialog
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
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso

class AgendaAdapter (private val context: Context, private val listServicos: ArrayList<Servico>, private val listServicosSolicitados: ArrayList<ServicosSolicitados>, private val servicosRecuperados:DatabaseReference) : RecyclerView.Adapter<AgendaAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_agenda,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listServicos.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val servico = listServicos.get(i)
        val servico2 = listServicosSolicitados.get(i)
        myViewHolder.titulo.text = servico.titulo
        myViewHolder.valor.text = servico.valor
        myViewHolder.descricao.text = servico.descricao


        //metodo de click
        myViewHolder.excluir.setOnClickListener {
            confirmarExclusao(myViewHolder,servico.id,servico2.id)
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
        var excluir : ImageButton

        init {
            titulo = itemView.findViewById(R.id.titulo_agenda)
            descricao =  itemView.findViewById(R.id.descricao_agenda)
            valor = itemView.findViewById(R.id.agenda_valor)
            foto = itemView.findViewById(R.id.image_agenda)
            excluir = itemView.findViewById(R.id.excluir_solicitado)

        }
    }

    fun confirmarExclusao(myViewHolder : AgendaAdapter.MyViewHolder, id: String,idSolicitado :String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Excluir Serviço")
        builder.setMessage("Você tem certeza que deseja EXCLUIR a solicitação do serviço: ${myViewHolder.titulo.text} ?")
        builder.setCancelable(false)

        builder.setPositiveButton("Excluir" ){ dialogInterface, i ->apagar(id, idSolicitado) }

        builder.setNegativeButton("Cancelar"){dialogInterface, i ->}

        val dialog = builder.create()
        dialog.show()
    }

    fun apagar(id: String, idSolicitado :String){
        servicosRecuperados.child(id).child("servicosSolicitados").child(idSolicitado).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                // procurarSolicitacao(id)
                Toast.makeText(context,"Serviço excluido com succeso", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Erro ao excluir o serviço!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}