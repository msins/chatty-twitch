package edu.fer.unizg.msins.chatty.chats

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun TabLayout.attachViewPager2(viewPager: ViewPager2, list: List<String>?) {
    TabLayoutMediator(this, viewPager) { tab, position ->
        tab.text = list?.get(position)
        viewPager.setCurrentItem(tab.position, true)
    }.attach()
}