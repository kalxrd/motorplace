package com.example.motorplace.fragments.cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.motorplace.R


class ServicosFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_servicos, container, false)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*  codigo para futura implementação
        val viewPager: ViewPager = view!!.findViewById(R.id.viewPager)
        val viewPagerTab: SmartTabLayout = view!!.findViewById(R.id.viewPagerTab)

        //Configurar abas
        val adapter = FragmentPagerItemAdapter(
            fragmentManager,
            FragmentPagerItems.with(context)
                .add("Revisões", RevisoesFragment::class.java)
                .add("Pintura e Funilaria", PinturaFragment::class.java)
                .create()
        )
        viewPager.adapter = adapter
        viewPagerTab.setViewPager(viewPager)


         */
    }
}