package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.motorplace.R
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_produtos_detalhes.*

class ProdutosDetalhesActivity : AppCompatActivity() {
    private lateinit var data : Bundle
    private lateinit var titulo: TextView
    private lateinit var valor: TextView
    private lateinit var descricao: TextView
    private lateinit var imagemPerfil: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos_detalhes)

        supportActionBar!!.title = "Produto"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        data = intent.extras!!
        inicializar()
        btn_adicionar_carrinho.setOnClickListener {
            adicionarCarrinho()
        }
    }

    fun adicionarCarrinho(){
        var database = FirebaseDatabase.getInstance().reference.child("usuarios").child(carroAtual.idUsuario).child("carrinho")

        database.child( data.get("id").toString()).child("id").setValue( data.get("id").toString())

        Toast.makeText(this, "Produto adicionado no carrinho", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun inicializar(){
        titulo = findViewById(R.id.text_titulo_produto)
        valor = findViewById(R.id.text_valor_produto)
        descricao = findViewById(R.id.text_descricao)

        imagemPerfil = findViewById(R.id.image_produto)


        titulo.text = data.get("titulo").toString()
        valor.text = data.get("valor").toString()
        descricao.text = data.get("descricao").toString()


        Picasso.get()
            .load(data.getString("foto")!!)
            .into(imagemPerfil)
    }
}