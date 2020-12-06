package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.motorplace.R
import kotlinx.android.synthetic.main.activity_favoritos.*

class FavoritosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        supportActionBar!!.title = "Favoritos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_favorito_servicos.setOnClickListener {
            startActivity(Intent(this,FavoritosServicosActivity::class.java))
        }

        btn_favorito_produtos.setOnClickListener {
            startActivity(Intent(this,FavoritosProdutosActivity::class.java))
        }
    }
}