package com.io.whatsapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.io.whatsapp.R
import com.io.whatsapp.databinding.FragmentHomeBinding
import com.io.whatsapp.ui.home.adapters.HomeTabsAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHomeViewPagerAndTabLayout()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun setUpHomeViewPagerAndTabLayout(){
        val adapter = HomeTabsAdapter(this)
        val tabTitles = listOf("", "CHATS", "STATUS", "CALLS")
        val tabIcon = listOf(context?.getDrawable(R.drawable.id_camera_black), null, null, null)


        with(binding){
            homeTabsViewPager.adapter = adapter
            
            TabLayoutMediator(homeTabsLayout, homeTabsViewPager){tabLayout, position ->
                tabLayout.apply {
                    text =tabTitles[position]
                    icon = tabIcon[position]
                }

                homeTabsViewPager.currentItem = 1


            }.attach()
        }
    }

}