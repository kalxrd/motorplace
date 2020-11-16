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
import com.example.motorplace.activitys.EditarServicoAdmActivity
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ServicosAdmAdapter (private val context: Context, private val listServicos: ArrayList<Servico>, private val servicosRecuperados:DatabaseReference, private val servicosExcluidos:DatabaseReference) : RecyclerView.Adapter<ServicosAdmAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_servicos_adm,viewGroup,false)
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
        myViewHolder.editar.setOnClickListener {
            val intent = Intent(context,EditarServicoAdmActivity::class.java)
            intent.putExtra("titulo",servico.titulo)
            intent.putExtra("descricao",servico.descricao)
            intent.putExtra("categoria",servico.categoria)
            intent.putExtra("valor",servico.valor)
            intent.putExtra("custo",servico.custo)
            intent.putExtra("id",servico.id)
            intent.putExtra("foto",servico.foto)

            context.startActivity(intent)
        }

        myViewHolder.excluir.setOnClickListener {
            confirmarExclusao(myViewHolder,servico.id)
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
        var excluir : ImageButton
        var editar : ImageButton

        init {
            titulo = itemView.findViewById(R.id.txt_titulo_list)
            valor = itemView.findViewById(R.id.txt_valor_list)
            foto = itemView.findViewById(R.id.image_servicos_adm_list)
            excluir = itemView.findViewById(R.id.btn_excluir_servico)
            editar = itemView.findViewById(R.id.btn_editar_servico)
        }
    }
    fun confirmarExclusao(myViewHolder : MyViewHolder, id: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Excluir Serviço")
        builder.setMessage("Você tem certeza que deseja EXCLUIR o serviço: ${myViewHolder.titulo.text} ?")
        builder.setCancelable(false)

        builder.setPositiveButton("Excluir" ){ dialogInterface, i ->apagar(id) }

        builder.setNegativeButton("Cancelar"){dialogInterface, i ->}

        val dialog = builder.create()
        dialog.show()
    }

    fun apagar(id: String){
        servicosRecuperados.child(id).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
               // procurarSolicitacao(id)
                Toast.makeText(context,"Serviço excluido com succeso",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Erro ao excluir o serviço!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun procurarSolicitacao(idServico:String){
        servicosExcluidos.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children){
                    val u = d.getValue(ServicosSolicitados::class.java)
                    if(u!!.idServico== idServico){

                    }
                }
            }

        })

    }
}