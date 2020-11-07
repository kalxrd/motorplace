package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.motorplace.R

class CadastrarProdutoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_produto)
        supportActionBar!!.title ="Cadastrar produto"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}