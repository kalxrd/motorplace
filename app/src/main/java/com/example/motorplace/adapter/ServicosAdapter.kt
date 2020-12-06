package com.example.motorplace.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.activitys.ServicoActivity
import com.example.motorplace.model.Servico
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class ServicosAdapter (private val context: Context, private val listServicos: ArrayList<Servico>) : RecyclerView.Adapter<ServicosAdapter.MyViewHolder>(){
    var favorito  = "false"
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

        val servicosFavorito =  FirebaseDatabase.getInstance().reference.child("servicos").child(servico.id).child("favoritos").child(
            carroAtual.idUsuario)



        inicializar(servicosFavorito, myViewHolder)
        //metodo de click
        myViewHolder.itemView.setOnClickListener {
            val intent = Intent(context,ServicoActivity::class.java)
            intent.putExtra("titulo",servico.titulo)
            intent.putExtra("descricao",servico.descricao)
            intent.putExtra("categoria",servico.categoria)
            intent.putExtra("valor",servico.valor)
            intent.putExtra("custo",servico.custo)
            intent.putExtra("id",servico.id)
            intent.putExtra("foto",servico.foto)
            context.startActivity(intent)
        }
        myViewHolder.favorito.setOnClickListener {
          // Toast.makeText(context,myViewHolder.favorito.getdra.toString(),Toast.LENGTH_SHORT).show()
            adicionarFavorito(servicosFavorito,myViewHolder,servico)
        }

        //pega a primeira imagem da lista
        Picasso.get()
            .load(servico?.foto)
            .into(myViewHolder.foto)

    }
    fun inicializar(favoritoReference: DatabaseReference, myViewHolder:MyViewHolder){
        favoritoReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    favorito = "true"
                    myViewHolder.favorito.setImageDrawable(context.resources.getDrawable(R.drawable.ic_coracao_content))
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
    fun adicionarFavorito(favoritoReference: DatabaseReference, myViewHolder:MyViewHolder, servico: Servico){
        if (favorito.equals("true")){
            favoritoReference.removeValue()
            myViewHolder.favorito.setImageDrawable(context.resources.getDrawable(R.drawable.ic_coracao))
            favorito = "false"
        }else{
            FirebaseDatabase.getInstance().reference.child("servicos").child(servico.id).child("favoritos").child(
                carroAtual.idUsuario).child("favorito").setValue("true")
            myViewHolder.favorito.setImageDrawable(context.resources.getDrawable(R.drawable.ic_coracao_content))
            favorito = "true"
        }
    }

    inner  class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo : TextView
        var valor : TextView
        var foto : ImageView
        var favorito : ImageView

        init {
            titulo = itemView.findViewById(R.id.txt_titulo_cliente_list)
            valor = itemView.findViewById(R.id.txt_valor_cliente)
            foto = itemView.findViewById(R.id.image_servicos_cliente)
            favorito = itemView.findViewById(R.id.servico_favorito)

        }
    }
}