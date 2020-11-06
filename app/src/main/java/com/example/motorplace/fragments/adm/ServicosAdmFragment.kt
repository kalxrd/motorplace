package com.example.motorplace.fragments.adm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.motorplace.R
import kotlinx.android.synthetic.main.fragment_servicos_adm.view.*


class ServicosAdmFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_servicos_adm, container, false)

        //criacao do spinner
        val spinnerFiltro = resources.getStringArray(R.array.filtro) //recupera o array do string.xml
        view.spinner_filtro_adm.setAdapter(
            ArrayAdapter(view.context,R.layout.support_simple_spinner_dropdown_item, spinnerFiltro)
        ) //seta o adapter no spinner
        return view
    }

}