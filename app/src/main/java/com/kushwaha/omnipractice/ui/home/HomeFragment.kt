package com.kushwaha.omnipractice.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.kushwaha.omnipractice.R
import com.kushwaha.omnipractice.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val scrollHandler = Handler(Looper.getMainLooper())

    private val flipRunnable = object : Runnable {
        override fun run() {
            val flipper = binding.actionFlipper
            val nextIndex = (flipper.displayedChild + 1) % flipper.childCount
            flipper.displayedChild = nextIndex
            scrollHandler.postDelayed(this, 5000)
        }
    }

    private lateinit var cardContainer: LinearLayout
    private lateinit var scrollContainer: LinearLayout
    private lateinit var viewAllButton: TextView
    private var isVertical = false

    private val cardData = listOf(
        Triple("Savings Account", "**** 1234", "₹1,24,350.00"),
        Triple("Visa Credit Card", "**** 5678", "Limit: ₹1,00,000"),
        Triple("MasterCard", "**** 9012", "Limit: ₹50,000")
    )

    private var horizontalScroll: HorizontalScrollView? = null
    private var verticalLayout: LinearLayout? = null

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Start auto-scroll after layout is drawn
            binding.actionFlipper.doOnPreDraw {
                scrollHandler.postDelayed(flipRunnable, 5000)
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
        scrollHandler.removeCallbacks(flipRunnable)
        _binding = null
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.childFragmentContainer, fragment)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cardContainer = view.findViewById(R.id.cardContainer)
        viewAllButton = view.findViewById(R.id.viewAllButton)
        scrollContainer = view.findViewById(R.id.rootLayout)
        horizontalScroll = view.findViewById(R.id.cardScroll)

        addCardsHorizontally()

        viewAllButton.setOnClickListener {
            if (!isVertical) {
                showCardsVertically()
                viewAllButton.text = "Show Less ^"
            } else {
                showCardsHorizontallyAgain()
                viewAllButton.text = "View All >"
            }
            isVertical = !isVertical
        }
    }

    private fun addCardsHorizontally() {
        cardContainer.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())

        for ((title, number, amount) in cardData) {
            val cardView = inflater.inflate(R.layout.item_card, cardContainer, false)
            cardView.findViewById<TextView>(R.id.cardTitle).text = title
            cardView.findViewById<TextView>(R.id.cardNumber).text = number
            cardView.findViewById<TextView>(R.id.cardAmount).text = amount
            cardContainer.addView(cardView)
        }
    }

    private fun showCardsVertically() {
        // Remove horizontal scroll
        horizontalScroll?.let { scrollContainer.removeView(it) }

        // Create vertical layout
        val inflater = LayoutInflater.from(requireContext())
        verticalLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        for ((title, number, amount) in cardData) {
            val cardView = inflater.inflate(R.layout.item_card, verticalLayout, false)
            cardView.findViewById<TextView>(R.id.cardTitle).text = title
            cardView.findViewById<TextView>(R.id.cardNumber).text = number
            cardView.findViewById<TextView>(R.id.cardAmount).text = amount
            verticalLayout?.addView(cardView)
        }

        val index = scrollContainer.indexOfChild(viewAllButton) + 1
        scrollContainer.addView(verticalLayout, index)
    }

    private fun showCardsHorizontallyAgain() {
        // Remove vertical layout
        verticalLayout?.let { scrollContainer.removeView(it) }

        // Re-add the original horizontal scroll view
        val index = scrollContainer.indexOfChild(viewAllButton) + 1
        scrollContainer.addView(horizontalScroll, index)

        // Re-populate cards
        addCardsHorizontally()
    }
}