package com.example.motorplace.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.motorplace.fragments.adm.EletricaFragment
import com.example.motorplace.fragments.adm.PinturaFragment
import com.example.motorplace.fragments.adm.PromocoesFragment
import com.example.motorplace.fragments.adm.RevisoesFragment

class TabsAdmAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {
                RevisoesFragment()
            }
            1 ->
                PinturaFragment()
            2 ->
                EletricaFragment()
            else -> {
                return PromocoesFragment()
            }
        }
    }



    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence{
        return when(position){
            0 -> "Revisões"
            1 -> "Pintura e Funilaria"
            2 -> "Elétrica"
            else -> {
                return "Promoções"
            }
        }

    }

}