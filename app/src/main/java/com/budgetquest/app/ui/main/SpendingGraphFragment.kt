package com.budgetquest.app.ui.main

import android.graphics.Color
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.budgetquest.app.databinding.FragmentSpendingGraphBinding
import com.budgetquest.app.ui.AppViewModel
import com.budgetquest.app.data.db.ExpenseEntity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.LocalDate

/**
 * Daily spending graph powered by MPAndroidChart.
 */
class SpendingGraphFragment : Fragment() {
    private var _binding: FragmentSpendingGraphBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()
    private var startDate = LocalDate.now().withDayOfMonth(1).toString()
    private var endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString()
    private var categoryMap = emptyMap<Long, Pair<String, String>>()
    private var expenseLiveData: LiveData<List<ExpenseEntity>>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSpendingGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.startDateGraph.text = startDate
        binding.endDateGraph.text = endDate
        binding.startDateGraph.setOnClickListener { pickDate { d -> startDate = d; binding.startDateGraph.text = d; bindChart() } }
        binding.endDateGraph.setOnClickListener { pickDate { d -> endDate = d; binding.endDateGraph.text = d; bindChart() } }

        vm.categoriesLive().observe(viewLifecycleOwner) { cats ->
            categoryMap = cats.associate { it.id to (it.name to it.colorHex) }
            bindChart()
        }
    }

    private fun bindChart() {
        expenseLiveData?.removeObservers(viewLifecycleOwner)
        expenseLiveData = vm.expensesLive(startDate, endDate)
        expenseLiveData?.observe(viewLifecycleOwner) { expenses ->
            val groupedByCategory = expenses.groupBy { it.categoryId }
            val dataSets = groupedByCategory.mapNotNull { (categoryId, list) ->
                val category = categoryMap[categoryId] ?: return@mapNotNull null
                val entries = list.groupBy { it.date.substring(8, 10).toFloat() }
                    .map { Entry(it.key, it.value.sumOf { e -> e.amountZar }.toFloat()) }
                    .sortedBy { it.x }
                LineDataSet(entries, category.first).apply {
                    color = try {
                        Color.parseColor(category.second)
                    } catch (_: Exception) {
                        randomFunColor(label.hashCode())
                    }
                    valueTextColor = Color.WHITE
                    lineWidth = 2.4f
                    setDrawCircles(true)
                    circleRadius = 3.5f
                    setDrawValues(false)
                }
            }
            if (dataSets.isEmpty()) {
                binding.graphLegend.text = "No spending in selected range"
                binding.chart.clear()
                return@observe
            }
            binding.graphLegend.text = "Fun trend lines by category (${dataSets.joinToString { it.label }})"
            binding.chart.data = LineData(dataSets)
            binding.chart.setBackgroundColor(Color.parseColor("#1F2344"))
            binding.chart.description.text = ""
            binding.chart.axisLeft.textColor = Color.WHITE
            binding.chart.axisRight.isEnabled = false
            binding.chart.xAxis.textColor = Color.WHITE
            binding.chart.legend.textColor = Color.WHITE
            binding.chart.axisLeft.gridColor = Color.parseColor("#33405A")
            binding.chart.xAxis.gridColor = Color.parseColor("#33405A")
            binding.chart.invalidate()
        }
    }

    private fun randomFunColor(seed: Int): Int {
        val palette = listOf("#FF8A65", "#4DD0E1", "#BA68C8", "#FFD54F", "#81C784", "#F06292")
        return Color.parseColor(palette[kotlin.math.abs(seed) % palette.size])
    }

    private fun pickDate(onPicked: (String) -> Unit) {
        val now = LocalDate.now()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            onPicked("%04d-%02d-%02d".format(y, m + 1, d))
        }, now.year, now.monthValue - 1, now.dayOfMonth).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
