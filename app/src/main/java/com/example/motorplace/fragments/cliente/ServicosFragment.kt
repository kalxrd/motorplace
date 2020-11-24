package com.example.motorplace.fragments.cliente

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.motorplace.R
import com.example.motorplace.adapter.TabsAdapter
import com.google.android.material.tabs.TabLayout
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems


class ServicosFragment : Fragment() {
    private lateinit var viewPagerTabs: ViewPager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_servicos, container, false)
        viewPagerTabs = view.findViewById(R.id.viewPager)


        val fragmentAdapter = TabsAdapter(childFragmentManager)
        viewPagerTabs.adapter = fragmentAdapter
        val viewPagerTab = view.findViewById(R.id.vie) as SmartTabLayout
        viewPagerTab.setViewPager(viewPagerTabs)



        return view
    }
}