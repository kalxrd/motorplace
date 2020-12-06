package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.motorplace.R
import com.squareup.picasso.Picasso

class ServicoAgendadoActivity : AppCompatActivity() {
    private lateinit var data : Bundle
    private lateinit var titulo: TextView
    private lateinit var valor: TextView
    private lateinit var dataServico: TextView
    private lateinit var horario: TextView
    private lateinit var imagemPerfil: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servico_agendado)

        supportActionBar!!.title = "Serviço agendado"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        data = intent.extras!!
        inicializar()
    }

    fun inicializar(){
        titulo = findViewById(R.id.text_titulo)
        valor = findViewById(R.id.text_valor)
        dataServico = findViewById(R.id.text_data)
        horario = findViewById(R.id.text_hora)

        imagemPerfil = findViewById(R.id.image_ser)


        titulo.text = data.get("titulo").toString()
        valor.text = data.get("valor").toString()
        dataServico.text = data.get("data").toString()
        horario.text = data.get("hora").toString() +"h"



        Picasso.get()
            .load(data.getString("foto")!!)
            .into(imagemPerfil)
    }
}