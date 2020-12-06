package com.example.motorplace.activitys

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.motorplace.R
import mehdi.sakout.aboutpage.AboutPage


class SobreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val texto =
            """
           O aplicativo mobile Motor Place foi desenvolvido com o objetivo de auxiliar nos processos da oficina, possibilitando mais praticidade através de simples cliques em seu aparelho celular. Além de gerenciar o desempenho da oficina automotiva, o app contém variadas funcionalidades disponíveis aos usuários que desejam conhecer sobre o estabelecimento, saber preços de serviços e produtos disponibilizados, esclarecer dúvidas com o administrador a até mesmo solicitar um serviço quando desejado. A criação do Motor Place foi planejada para você, nosso principal cliente! Então disponha das oportunidades que lhe oferecemos com tanta dedicação. 
            """.trimIndent()

        val aboutPage: View = AboutPage(this)
            .setImage(R.drawable.logomotor)
            .addGroup("Fale conosco")
            .addEmail("motorplace@gmail.com", "Envie um e-mail")
            .addGroup("Acesse nosso Github")
            .setDescription(texto)
            .addGitHub("leSant31/MotorPlace", "Github")
            .enableDarkMode(false)
            .create()

        supportActionBar!!.setTitle("Sobre")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setContentView(aboutPage)
    }
}