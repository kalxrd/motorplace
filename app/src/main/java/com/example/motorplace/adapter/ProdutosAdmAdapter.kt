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
import com.example.motorplace.activitys.EditarServicoAdmActivity
import com.example.motorplace.model.Produto
import com.example.motorplace.model.Servico
import com.google.firebase.database.DatabaseReference

import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class ProdutosAdmAdapter (private val context: Context, private val listProdutos: ArrayList<Produto>, private val produtosRecuperados:DatabaseReference) : RecyclerView.Adapter<ProdutosAdmAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_view_produtos_adm,viewGroup,false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listProdutos.size
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val produto = listProdutos.get(i)
        myViewHolder.titulo.text = produto.titulo
        myViewHolder.valor.text = produto.valor


        //metodo de click
        myViewHolder.editar.setOnClickListener {
            val intent = Intent(context,EditarProdutoAdmActivity::class.java)
            intent.putExtra("titulo",produto.titulo)
            intent.putExtra("descricao",produto.descricao)
            intent.putExtra("marca",produto.marca)
            intent.putExtra("alerta",produto.alertaQtdMin)
            intent.putExtra("qtdEstoque",produto.qtdEstoque)
            intent.putExtra("valor",produto.valor)
            intent.putExtra("custo",produto.custo)
            intent.putExtra("id",produto.id)
            intent.putExtra("foto",produto.foto)

            context.startActivity(intent)
        }

        myViewHolder.excluir.setOnClickListener {
            confirmarExclusao(myViewHolder,produto.id)
        }


        //pega a primeira imagem da lista
        Picasso.get()
            .load(produto?.foto)
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
            foto = itemView.findViewById(R.id.image_produtos_adm_list)
            excluir = itemView.findViewById(R.id.btn_excluir_produto)
            editar = itemView.findViewById(R.id.btn_editar_produto)
        }
    }
    fun confirmarExclusao(myViewHolder : MyViewHolder, id: String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Excluir Serviço")
        builder.setMessage("Você tem certeza que deseja EXCLUIR o produto: ${myViewHolder.titulo.text} ?")
        builder.setCancelable(false)

        builder.setPositiveButton("Excluir" ){ dialogInterface, i ->apagar(id) }

        builder.setNegativeButton("Cancelar"){dialogInterface, i ->}

        val dialog = builder.create()
        dialog.show()
    }

    fun apagar(id: String){
        produtosRecuperados.child(id).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context,"Produto excluido com succeso",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Erro ao excluir o serviço!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}