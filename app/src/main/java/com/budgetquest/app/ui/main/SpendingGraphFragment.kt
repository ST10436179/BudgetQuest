package com.budgetquest.app.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.budgetquest.app.databinding.FragmentSpendingGraphBinding
import com.budgetquest.app.ui.AppViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

/**
 * Daily spending graph powered by MPAndroidChart.
 */
class SpendingGraphFragment : Fragment() {
    private var _binding: FragmentSpendingGraphBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSpendingGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.expensesLive("2000-01-01", "2999-12-31").observe(viewLifecycleOwner) { expenses ->
            val grouped = expenses.groupBy { it.date.substring(8, 10).toFloat() }
            val entries = grouped.map { Entry(it.key, it.value.sumOf { e -> e.amountZar }.toFloat()) }.sortedBy { it.x }
            val set = LineDataSet(entries, "Total Spend").apply {
                color = Color.YELLOW
                valueTextColor = Color.WHITE
                lineWidth = 2f
            }
            binding.chart.data = LineData(set)
            binding.chart.description.text = "Daily breakdown by category"
            binding.chart.invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
