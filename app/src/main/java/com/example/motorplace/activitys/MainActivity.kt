package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import com.example.motorplace.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //método para esconder o ActionBar
        supportActionBar!!.hide()

        //Para o Splash ocupar toda a tela do celular
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        var isAdm  = false
        var reference = FirebaseDatabase.getInstance().reference

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            reference.child("usuarios").child(user.uid).child("adm").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val adm = dataSnapshot.value
                    if (adm.toString().equals("true")) {
                        isAdm = true
                    }
                    if(isAdm){
                        Handler().postDelayed({
                            //metodo para trocar de tela
                            val intent = Intent(applicationContext, HomeAdmActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent)
                        }, 2000)
                    }else{
                        Handler().postDelayed({
                            //metodo para trocar de tela
                            val intent = Intent(applicationContext, TelaHomeCliente::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent)
                        }, 2000)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(applicationContext, "ERRO COM INTERNET", Toast.LENGTH_LONG).show()
                    auth.signOut()
                    finish()
                }
            })
        } else {
            Handler().postDelayed({
                //metodo para trocar de tela
                val intent = Intent(applicationContext, TelaDeEntradaActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }, 4000)
        }

    }
}