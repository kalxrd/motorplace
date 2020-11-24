package com.example.motorplace.fragments.adm

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.motorplace.R
import com.example.motorplace.activitys.CadastroPromocaoActivity
import kotlinx.android.synthetic.main.fragment_home_adm.view.*

class HomeAdmFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_home_adm, container, false)


        view.btn_cadastrar_promocao.setOnClickListener {
            startActivity(Intent(context,CadastroPromocaoActivity::class.java))
        }

        return view
    }
}