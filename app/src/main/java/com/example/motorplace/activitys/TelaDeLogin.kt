package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.motorplace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_tela_de_login.*

class TelaDeLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_de_login)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btnLogin.setOnClickListener{
            Login()
        }

        btnResetSenha.setOnClickListener {
            startActivity(Intent(this, TelaRecuperarSenha::class.java))
        }

    }

    private fun Login(){

        var builder = android.app.AlertDialog.Builder(this).create()
        var view = layoutInflater.inflate(R.layout.progress_bar, null)
        builder.setView(view)
        builder.show()

        val auth = FirebaseAuth.getInstance()
        val reference = FirebaseDatabase.getInstance().reference
        val email = editTextEmailL.text.toString()
        val senha = editTextSenhaL.text.toString()
        var isAdm  = false

        auth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
            val user = auth.currentUser
            val uid = auth.uid


            reference.child("adms").child(uid.toString()).child("tipoU").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val adm = dataSnapshot.value
                    if (adm.toString().equals("true")) {
                        isAdm = true
                    }
                    if(isAdm){
                        startActivity(Intent(applicationContext, TelaHomeAdm::class.java))
                    }else{
                        startActivity(Intent(applicationContext, TelaHomeCliente::class.java))
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "ERRO NOS DADOS", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}