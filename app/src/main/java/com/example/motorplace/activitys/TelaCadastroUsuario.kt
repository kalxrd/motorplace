package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import com.example.motorplace.R
import com.example.motorplace.entities.Adm
import com.example.motorplace.entities.Cliente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_tela_cadastro_usuario.*

class TelaCadastroUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_cadastro_usuario)
        supportActionBar!!.title ="Cadastro"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        prosseguirCadastro.setOnClickListener{
            cadastrarUsuario()
        }

    }

    private fun cadastrarUsuario(){

        var builder = android.app.AlertDialog.Builder(this).create()
        var view = layoutInflater.inflate(R.layout.progress_bar, null)
        builder.setView(view)
        builder.show()

        val nome = editTextNome.text.toString()
        val cpf = editTextCpf.text.toString()
        val data = editTextDataN.text.toString()
        val email = editTextEmail.text.toString()
        val telefone = editTextTel.text.toString()
        val senha = editTextSenha.text.toString()
        val senhaC = editTextSenhac.text.toString()
        val tipo = findViewById<Switch>(R.id.switchTipo).isChecked();

        if(nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || data.isEmpty() || email.isEmpty() || senha.isEmpty() || senhaC.isEmpty()) {
        }

        val auth = FirebaseAuth.getInstance()
        val reference = FirebaseDatabase.getInstance().reference

        auth.createUserWithEmailAndPassword(email, senha).addOnSuccessListener {
            Toast.makeText(applicationContext, "Email cadastrado", Toast.LENGTH_LONG).show()

            val user = auth.currentUser
            val uid = auth.uid

            if(tipo){
                val adm = Adm(nome, cpf, data, telefone,tipo, uid)
                reference.child("adms").child(adm.id.toString()).setValue(adm).addOnSuccessListener {
                    Toast.makeText(applicationContext, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()
                }
            }else{
                val cliente = Cliente(nome, cpf, data, telefone,tipo, uid)
                reference.child("clientes").child(cliente.id.toString()).setValue(cliente).addOnSuccessListener {
                    Toast.makeText(applicationContext, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()
                }
            }
        }

        auth.signOut()
        val intent = Intent(this, TelaDeLogin::class.java)
        startActivity(intent)
    }


}

