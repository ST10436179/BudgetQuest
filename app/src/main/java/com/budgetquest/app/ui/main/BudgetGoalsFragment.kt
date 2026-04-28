package com.budgetquest.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.databinding.FragmentBudgetGoalsBinding
import com.budgetquest.app.ui.AppViewModel

/**
 * Monthly min/max and category limit entry screen.
 */
class BudgetGoalsFragment : Fragment() {
    private var _binding: FragmentBudgetGoalsBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()
    private val perCategoryInputs = mutableMapOf<Long, EditText>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBudgetGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        vm.categoriesLive().observe(viewLifecycleOwner) { cats ->
            binding.categoryLimitContainer.removeAllViews()
            perCategoryInputs.clear()
            cats.forEach { cat ->
                val row = LinearLayout(requireContext()).apply { orientation = LinearLayout.VERTICAL }
                row.addView(TextView(requireContext()).apply { text = "${cat.emoji} ${cat.name}"; setTextColor(android.graphics.Color.WHITE) })
                val input = EditText(requireContext()).apply { hint = "R amount"; setTextColor(android.graphics.Color.WHITE) }
                row.addView(input)
                binding.categoryLimitContainer.addView(row)
                perCategoryInputs[cat.id] = input
            }
        }
        binding.saveBtn.setOnClickListener {
            try {
                val min = binding.minGoal.text?.toString()?.toDoubleOrNull() ?: 0.0
                val max = binding.maxGoal.text?.toString()?.toDoubleOrNull() ?: 0.0
                val map = perCategoryInputs.mapValues { it.value.text?.toString()?.toDoubleOrNull() ?: 0.0 }
                vm.saveGoals(min, max, map)
                binding.errorText.text = "Saved"
            } catch (_: Exception) {
                binding.errorText.text = "Invalid input"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
