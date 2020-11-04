package com.example.motorplace.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.motorplace.R

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

        Handler().postDelayed({
            //metodo para trocar de tela
            startActivity(Intent(applicationContext, TelaDeEntradaActivity::class.java))
        }, 4000)
    }
}