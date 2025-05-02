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

            // Scroll forward until end, then reset
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

        // Start auto-scroll after view is ready
        binding.autoScrollView.doOnPreDraw {
            scrollHandler.postDelayed(scrollRunnable, 5000)
        }

        binding.tabRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.layoutTransact.visibility = View.GONE
            binding.layoutInvest.visibility = View.GONE
            binding.layoutLoans.visibility = View.GONE

            when (checkedId) {
                R.id.tabHome -> binding.layoutTransact.visibility = View.VISIBLE
                R.id.tabShop -> binding.layoutInvest.visibility = View.VISIBLE
                R.id.tabNew -> binding.layoutLoans.visibility = View.VISIBLE
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scrollHandler.removeCallbacks(scrollRunnable)
        _binding = null
    }

}
