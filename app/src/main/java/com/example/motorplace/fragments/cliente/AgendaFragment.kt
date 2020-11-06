package com.example.motorplace.fragments.cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.motorplace.R
import kotlinx.android.synthetic.main.fragment_agenda.view.*


class AgendaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_agenda, container, false)


        //criacao do spinner
        val spinnerFiltro = resources.getStringArray(R.array.filtro) //recupera o array do string.xml
        view.spinner_filtro.setAdapter(ArrayAdapter(context!!,
            R.layout.support_simple_spinner_dropdown_item, spinnerFiltro)) //seta o adapter no spinner
        return view
    }
}

