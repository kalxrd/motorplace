package com.example.motorplace.activitys

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.motorplace.R
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.util.carroAtual
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_editar_servico_adm.*
import kotlinx.android.synthetic.main.activity_servico.*

class ServicoActivity : AppCompatActivity() {
    private lateinit var modelo :TextView
    private lateinit var tamanho :TextView
    private lateinit var valor :TextView
    private lateinit var titulo :TextView
    private lateinit var dados :Bundle
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servico)
        supportActionBar!!.title = "Serviço"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        inicializar()

        btn_solicitar_servico.setOnClickListener {
            cadastroServico()
        }
    }

    private fun inicializar(){
        modelo = findViewById(R.id.txt_modelo)
        tamanho = findViewById(R.id.txt_tamanho)
        valor = findViewById(R.id.valor)
        titulo = findViewById(R.id.titulo_solicitar)
        dados = intent.extras!!
        database = FirebaseDatabase.getInstance().reference

        modelo.text = "Modelo: ${carroAtual.modelo}"
        tamanho.text = "Tamanho: ${carroAtual.tamanho}"
        valor.text = dados.getString("valor")
        titulo.text = dados.getString("titulo")

        Picasso.get()
            .load(dados.getString("foto"))
            .into(image_solicitar_servico)
    }

    fun cadastroServico(){
        val pd = ProgressDialog(this)
        pd.setMessage("Salvando...")
        pd.show()

        val servicos = ServicosSolicitados()
        servicos.idCliente = carroAtual.idUsuario
        servicos.idServico = dados.getString("id")!!

        val idServicosSolicitados =  database.child("servicosSolicitados").push().key
        servicos.id = idServicosSolicitados.toString()
        database.child("servicosSolicitados").child(idServicosSolicitados.toString()).setValue(servicos).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this,TelaHomeCliente::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(this,"Serviço solicitado com Sucesso",Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }

        }

    }
}