package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.motorplace.R
import kotlinx.android.synthetic.main.activity_cadastro_carro.*

class TelaCadastroCarro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_carro)
        supportActionBar!!.title ="Cadastro de Veículo"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        cadastrarVeiculo.setOnClickListener{
            startActivity(Intent(this, TelaHomeAdm::class.java))
        }
    }
}