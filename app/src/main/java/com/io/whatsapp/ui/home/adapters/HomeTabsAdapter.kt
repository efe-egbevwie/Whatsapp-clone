package com.io.whatsapp.ui.home.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.io.whatsapp.ui.home.CallsFragment
import com.io.whatsapp.ui.home.ChatsFragment
import com.io.whatsapp.ui.home.UploadStatusFragment

class HomeTabsAdapter(hostFragment: Fragment):FragmentStateAdapter(hostFragment) {

    private val fragments = listOf(UploadStatusFragment(), ChatsFragment(), CallsFragment())

    override fun getItemCount()= fragments.size

    override fun createFragment(position: Int)= fragments[position]
}