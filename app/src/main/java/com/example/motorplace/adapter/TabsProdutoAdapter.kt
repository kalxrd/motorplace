package com.example.motorplace.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.motorplace.fragments.cliente.FerramentasFragment
import com.example.motorplace.fragments.cliente.LimpezaFragment
import com.example.motorplace.fragments.cliente.ManutencaoFragment

class TabsProdutoAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {
                ManutencaoFragment()
            }
            1 ->
                LimpezaFragment()
            else -> {
                return FerramentasFragment()
            }
        }
    }



    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence{
        return when(position){
            0 -> "Manutenção"
            1 -> "Limpeza"
            else -> {
                return "Ferramentas"
            }
        }

    }

}