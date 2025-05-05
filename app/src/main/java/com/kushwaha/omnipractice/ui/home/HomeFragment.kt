package com.kushwaha.omnipractice.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.kushwaha.omnipractice.R
import com.kushwaha.omnipractice.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val scrollHandler = Handler(Looper.getMainLooper())
    private var scrollXPos = 0

    private val scrollRunnable = object : Runnable {
        override fun run() {
            val scrollView = binding.autoScrollView
            val maxScroll = scrollView.getChildAt(0).width - scrollView.width

            scrollXPos += 300
            if (scrollXPos >= maxScroll) {
                scrollXPos = 0
            }

            scrollView.smoothScrollTo(scrollXPos, 0)
            scrollHandler.postDelayed(this, 5000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Start auto-scroll after layout is drawn
        binding.autoScrollView.doOnPreDraw {
            scrollHandler.postDelayed(scrollRunnable, 5000)
        }

        // Load TransactFragment by default
        replaceFragment(TransactFragment())
        binding.tabRadioGroup.check(R.id.tabTransact) // set default selected

        //  Switch fragments based on selected tab
        binding.tabRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.tabTransact -> replaceFragment(TransactFragment())
                R.id.tabInvest -> replaceFragment(InvestFragment())
                R.id.tabLoans -> replaceFragment(LoansFragment())
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scrollHandler.removeCallbacks(scrollRunnable)
        _binding = null
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.childFragmentContainer, fragment)
            .commit()
    }
}