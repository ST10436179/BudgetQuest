package com.budgetquest.app.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.R
import com.budgetquest.app.data.db.ExpenseEntity
import com.budgetquest.app.databinding.FragmentExpenseListBinding
import com.budgetquest.app.ui.AppViewModel
import com.budgetquest.app.util.FormatUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Expense history with date filtering and simple paging button.
 */
class ExpenseListFragment : Fragment() {
    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()
    private var startDate = LocalDate.now().withDayOfMonth(1).toString()
    private var endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString()
    private var allExpenses = emptyList<ExpenseEntity>()
    private var displayCount = 8

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.startDate.setText(startDate)
        binding.endDate.setText(endDate)
        binding.startDate.setOnClickListener {
            pickDate { d ->
                startDate = d
                binding.startDate.setText(d)
                displayCount = 8
                load()
            }
        }
        binding.endDate.setOnClickListener {
            pickDate { d ->
                endDate = d
                binding.endDate.setText(d)
                displayCount = 8
                load()
            }
        }
        binding.addExpenseBtn.setOnClickListener { findNavController().navigate(R.id.addEditExpenseFragment) }
        binding.loadMore.setOnClickListener {
            if (displayCount < allExpenses.size) {
                displayCount += 8
                renderList()
            } else {
                binding.loadMore.text = "All entries loaded"
            }
        }
        load()
    }

    private fun load() {
        vm.expensesLive(startDate, endDate).observe(viewLifecycleOwner) { list ->
            allExpenses = list
            renderList()
        }
    }

    private fun renderList() {
        binding.listContainer.removeAllViews()
        val visible = allExpenses.take(displayCount)
        binding.entryCountText.text = "${allExpenses.size} ENTRIES"
        binding.totalText.text = "Total: ${FormatUtils.zar(allExpenses.sumOf { it.amountZar })}"

        visible.forEachIndexed { index, e ->
                val row = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    minimumHeight = 74
                    setPadding(6, 10, 6, 10)
                    setOnClickListener {
                        val bundle = Bundle().apply { putString("expenseId", e.id.toString()) }
                        findNavController().navigate(R.id.addEditExpenseFragment, bundle)
                    }
                }

                val dayMonthCol = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(0, 0, 14, 0)
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                val day = e.date.takeLast(2)
                val month = parseMonthShort(e.date)
                val dayView = TextView(requireContext()).apply {
                    text = day
                    setTextColor(resources.getColor(R.color.bq_accent, null))
                    textSize = 20f
                    setTypeface(typeface, android.graphics.Typeface.BOLD)
                }
                val monthView = TextView(requireContext()).apply {
                    text = month
                    setTextColor(resources.getColor(R.color.bq_text_muted, null))
                    textSize = 10f
                    setTypeface(typeface, android.graphics.Typeface.BOLD)
                }
                dayMonthCol.addView(dayView)
                dayMonthCol.addView(monthView)

                val contentCol = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                }
                val title = TextView(requireContext()).apply {
                    text = if (e.description.isBlank()) "Expense entry" else e.description
                    setTextColor(resources.getColor(R.color.bq_text_dark, null))
                    textSize = 18f
                    setTypeface(typeface, android.graphics.Typeface.BOLD)
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }
                val meta = TextView(requireContext()).apply {
                    text = "${e.date} • ${e.startTime}-${e.endTime}"
                    setTextColor(resources.getColor(R.color.bq_text_muted, null))
                    textSize = 12f
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }
                contentCol.addView(title)
                contentCol.addView(meta)

                val amount = TextView(requireContext()).apply {
                    text = FormatUtils.zar(e.amountZar)
                    setTextColor(resources.getColor(R.color.bq_accent, null))
                    textSize = 17f
                    setTypeface(typeface, android.graphics.Typeface.BOLD)
                }
                row.addView(dayMonthCol)
                row.addView(contentCol)
                row.addView(amount)
                binding.listContainer.addView(row)

                if (index != visible.lastIndex) {
                    val divider = View(requireContext()).apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
                        setBackgroundColor(resources.getColor(R.color.bq_input_border, null))
                    }
                    binding.listContainer.addView(divider)
                }
        }

        binding.loadMore.text = if (visible.size < allExpenses.size) {
            "Load more..."
        } else {
            "No more entries"
        }
        binding.loadMore.isEnabled = true
    }

    private fun parseMonthShort(date: String): String {
        return try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
                .month
                .name
                .take(3)
        } catch (_: Exception) {
            "MON"
        }
    }

    private fun pickDate(onDate: (String) -> Unit) {
        val now = LocalDate.now()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            onDate("%04d-%02d-%02d".format(y, m + 1, d))
        }, now.year, now.monthValue - 1, now.dayOfMonth).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
