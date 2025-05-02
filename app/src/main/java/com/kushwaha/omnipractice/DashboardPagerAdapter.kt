//package com.kushwaha.omnipractice
//
//import androidx.fragment.app.Fragment
//import androidx.viewpager2.adapter.FragmentStateAdapter
//
//
//class DashboardPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
//    override fun getItemCount(): Int = 3
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            0 -> TransactFragment()
//            1 -> InvestFragment()
//            2 -> LoansFragment()
//            else -> TransactFragment()
//        }
//    }
//}
