package com.budgetquest.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.budgetquest.app.databinding.FragmentCategoryTotalsBinding

/**
 * Category totals summary placeholder wired for tab navigation.
 */
class CategoryTotalsFragment : Fragment() {
    private var _binding: FragmentCategoryTotalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryTotalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
