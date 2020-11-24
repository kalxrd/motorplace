package com.example.motorplace.fragments.cliente


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.motorplace.R
import com.example.motorplace.util.Permissao
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    private val permisssaoLigacao= arrayOf(Manifest.permission.CALL_PHONE)
    private val SELECAO_PHONE = 100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_home, container, false)

        view.btn_guincho.setOnClickListener {
            confirmarLigacao()
        }

        view.btn_wpp.setOnClickListener {
            abrirWpp()
        }

        return view
    }

    private fun abrirWpp(){
        var url = "https:/api.whatsapp.com/send?phone=559288540875"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun ligarGuincho(){
        val uri  = Uri.parse("tel:988540875")
        val intent = Intent(Intent.ACTION_DIAL,uri)
        startActivity(intent)
    }

    fun confirmarLigacao(){
        if (Permissao.validarPermissao(permisssaoLigacao, activity, SELECAO_PHONE)) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Ligar para Guincho")
            builder.setMessage("Você tem certeza que deseja LIGAR para o guincho?")
            builder.setCancelable(false)

            builder.setPositiveButton("Ligar"){ dialogInterface, i ->ligarGuincho() }

            builder.setNegativeButton("Cancelar"){ dialogInterface, i ->}

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao()

            }else if (requestCode == SELECAO_PHONE) { //se foi aceita a permissao ira abrir a opcao da camera ou galeria
                val uri  = Uri.parse("tel:92988540875")
                val intent = Intent(Intent.ACTION_DIAL,uri)
                startActivity(intent)
            }
        }
    }


    fun alertaPermissao(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Permissão Negada")
        builder.setMessage("Para poder acessar este recurso é neceessário aceitar a permissão")
        builder.setCancelable(false)
        builder.setPositiveButton("OK"){ dialogInterface, i ->  }


        val dialog = builder.create()
        dialog.show()
    }

}