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
import com.example.motorplace.model.Servico
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ProdutosAdapter (private val context: Context, private val listProdutos: ArrayList<Produto>, private val produtosRecuperados: DatabaseReference) : RecyclerView.Adapter<ProdutosAdapter.MyViewHolder>(){
    var favorito  = "false"
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

        val produtosFavorito =  FirebaseDatabase.getInstance().reference.child("produtos").child(produto.id).child("favoritos").child(
            carroAtual.idUsuario)

        inicializar(produtosFavorito, myViewHolder)

        //pega a primeira imagem da lista
        Picasso.get()
            .load(produto?.foto)
            .into(myViewHolder.foto)

        myViewHolder.favorito.setOnClickListener {
            // Toast.makeText(context,myViewHolder.favorito.getdra.toString(),Toast.LENGTH_SHORT).show()
            adicionarFavorito(produtosFavorito,myViewHolder,produto)
        }

        myViewHolder.itemView.setOnClickListener{
            adicionarCarrinho(produto.id)
        }

    }

    fun adicionarCarrinho(id:String){
        var database = FirebaseDatabase.getInstance().reference.child("usuarios").child(carroAtual.idUsuario).child("carrinho")

        database.child(id).child("id").setValue(id)

        Toast.makeText(context, "Produto adicionado no carrinho",Toast.LENGTH_SHORT).show()
    }

    fun inicializar(favoritoReference: DatabaseReference, myViewHolder: ProdutosAdapter.MyViewHolder){
        favoritoReference.addValueEventListener(object : ValueEventListener {
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
    fun adicionarFavorito(favoritoReference: DatabaseReference, myViewHolder: ProdutosAdapter.MyViewHolder, produto: Produto){
        if (favorito.equals("true")){
            favoritoReference.removeValue()
            myViewHolder.favorito.setImageDrawable(context.resources.getDrawable(R.drawable.ic_coracao))
            favorito = "false"
        }else{
            FirebaseDatabase.getInstance().reference.child("produtos").child(produto.id).child("favoritos").child(
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