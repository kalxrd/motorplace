package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.motorplace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_tela_de_login.*

class TelaDeLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var reference:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_de_login)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference
        btnLogin.setOnClickListener{
            login()
        }

        btnResetSenha.setOnClickListener {
            startActivity(Intent(this, TelaRecuperarSenha::class.java))
        }

    }

    private fun login(){
        var email = editTextEmailL.text.toString()
        var senha = editTextSenhaL.text.toString()
        var valido = true

        if (email.isEmpty()){
            editTextEmailL.error = "Campo vazio!"
            valido = false
        }

        if(senha.isEmpty()){
            editTextSenhaL.error = "Campo vazio!"
            valido = false
        }

        if(valido){
            var builder = android.app.AlertDialog.Builder(this).create()
            var view = layoutInflater.inflate(R.layout.progress_bar, null)
            builder.setView(view)
            builder.show()

            var isAdm  = false

            auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener {
                if(it.isSuccessful){
                    val userUid = auth.currentUser!!.uid

                    reference.child("usuarios").child(userUid).child("adm").addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val adm = dataSnapshot.value
                            if (adm.toString().equals("true")) {
                                isAdm = true
                            }
                            if(isAdm){
                                val intent = Intent(applicationContext, TelaHomeAdm::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent)
                            }else{
                                val intent = Intent(applicationContext, TelaHomeCliente::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent)
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(applicationContext, "ERRO NOS DADOS", Toast.LENGTH_LONG).show()
                            auth.signOut()
                        }
                    })
                }else{
                    try {
                        throw it.exception!!
                    }catch (e: FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(applicationContext,"Credenciais inválidas!",Toast.LENGTH_SHORT).show()
                    }catch (e: FirebaseAuthException){
                        Toast.makeText(applicationContext,"Email nao cadastrado!",Toast.LENGTH_SHORT).show()
                    }catch (e:Exception){
                        Toast.makeText(applicationContext,"Erro ao realizar login: " +e.message,Toast.LENGTH_SHORT).show()
                    }
                    builder.dismiss()
                }
            }
        }

    }
}