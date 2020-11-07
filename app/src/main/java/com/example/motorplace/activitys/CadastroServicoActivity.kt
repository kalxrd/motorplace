package com.example.motorplace.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.motorplace.R
import kotlinx.android.synthetic.main.activity_cadastro_servico.*

class CadastroServicoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_servico)
        supportActionBar!!.title ="Cadastrar Serviço"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        //criacao do spinner
        val spinnerFiltro = resources.getStringArray(R.array.categoria) //recupera o array do string.xml
        spinner_cadastro_servicos.setAdapter(
            ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, spinnerFiltro)
        ) //seta o adapter no spinner
    }
}