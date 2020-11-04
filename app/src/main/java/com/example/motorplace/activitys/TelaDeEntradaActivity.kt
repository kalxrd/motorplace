package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.motorplace.R
import kotlinx.android.synthetic.main.activity_tela_de_entrada.*

class TelaDeEntradaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_de_entrada)
        supportActionBar!!.hide()

        btn_login.setOnClickListener{
            startActivity(Intent(this, TelaDeLogin::class.java))
        }
        btn_cadastrar.setOnClickListener{
            startActivity(Intent(this, TelaCadastroUsuario::class.java))
        }
    }

}
