package com.example.motorplace.fragments.adm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.motorplace.R
import com.example.motorplace.adapter.TabsAdmAdapter
import com.ogaclejapan.smarttablayout.SmartTabLayout



class ServicosAdmFragment : Fragment(){
    private lateinit var viewPagerTabs: ViewPager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_servicos_adm, container, false)

        viewPagerTabs = view.findViewById(R.id.viewPagerAdm)


        val fragmentAdapter = TabsAdmAdapter(childFragmentManager)
        viewPagerTabs.adapter = fragmentAdapter
        val viewPagerTab = view.findViewById(R.id.vie_adm) as SmartTabLayout
        viewPagerTab.setViewPager(viewPagerTabs)


        return view
    }
}