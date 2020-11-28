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
import com.example.motorplace.activitys.EditarPromocaoActivity
import com.example.motorplace.model.Servico
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class ServicosPromocoesAdm (private val context: Context, private val listPromocoes: ArrayList<Servico>, private val promocaosRecuperados:DatabaseReference) : RecyclerView.Adapter<ServicosPromocoesAdm.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_servicos_adm,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listPromocoes.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val promocao = listPromocoes.get(i)
        myViewHolder.titulo.text = promocao.titulo
        myViewHolder.valor.text = promocao.valor


        //metodo de click
        myViewHolder.editar.setOnClickListener {
            val intent = Intent(context,EditarPromocaoActivity::class.java)
            intent.putExtra("titulo",promocao.titulo)
            intent.putExtra("descricao",promocao.descricao)
            intent.putExtra("categoria",promocao.categoria)
            intent.putExtra("prazo",promocao.prazo)
            intent.putExtra("oferta",promocao.valor)
            intent.putExtra("id",promocao.id)
            intent.putExtra("foto",promocao.foto)

            context.startActivity(intent)
        }

        myViewHolder.excluir.setOnClickListener {
            confirmarExclusao(myViewHolder,promocao.id)
        }


        //pega a primeira imagem da lista
        Picasso.get()
            .load(promocao?.foto)
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
        promocaosRecuperados.child(id).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                // procurarSolicitacao(id)
                Toast.makeText(context,"Serviço excluido com succeso",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Erro ao excluir o serviço!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}