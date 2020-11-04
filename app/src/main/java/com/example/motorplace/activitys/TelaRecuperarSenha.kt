package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.motorplace.R
import kotlinx.android.synthetic.main.activity_recuperar_senha.*

class TelaRecuperarSenha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)
        supportActionBar!!.title ="Recuperar senha "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_emailRecuperacao.setOnClickListener {
            recuperarSenha()
        }

    }

    private fun recuperarSenha(){

        val auth = FirebaseAuth.getInstance()
        var email = editTextRecuperarEmail.text.toString()

        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            Toast.makeText(applicationContext, "Email enviado", Toast.LENGTH_LONG).show()
        }
    }
}
