package com.budgetquest.app.ui.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.R
import com.budgetquest.app.databinding.FragmentExpenseListBinding
import com.budgetquest.app.ui.AppViewModel
import com.budgetquest.app.util.FormatUtils
import java.time.LocalDate

/**
 * Expense history with date filtering and simple paging button.
 */
class ExpenseListFragment : Fragment() {
    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()
    private var startDate = LocalDate.now().withDayOfMonth(1).toString()
    private var endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.startDate.setText(startDate)
        binding.endDate.setText(endDate)
        binding.startDate.setOnClickListener { pickDate { d -> startDate = d; binding.startDate.setText(d); load() } }
        binding.endDate.setOnClickListener { pickDate { d -> endDate = d; binding.endDate.setText(d); load() } }
        binding.loadMore.text = "Load more..."
        load()
    }

    private fun load() {
        vm.expensesLive(startDate, endDate).observe(viewLifecycleOwner) { list ->
            binding.listContainer.removeAllViews()
            binding.entryCountText.text = "${list.size} ENTRIES"
            binding.totalText.text = "Total: ${FormatUtils.zar(list.sumOf { it.amountZar })}"

            list.take(50).forEachIndexed { index, e ->
                val row = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(8, 12, 8, 12)
                    setOnClickListener {
                        val bundle = Bundle().apply { putString("expenseId", e.id.toString()) }
                        findNavController().navigate(R.id.addEditExpenseFragment, bundle)
                    }
                }

                val day = e.date.takeLast(2)
                val dayView = TextView(requireContext()).apply {
                    text = day
                    setTextColor(resources.getColor(R.color.bq_accent, null))
                    textSize = 24f
                    setPadding(0, 0, 18, 0)
                }
                val desc = TextView(requireContext()).apply {
                    text = "${e.description}\n${e.date} • ${e.startTime}-${e.endTime}"
                    setTextColor(resources.getColor(R.color.bq_text_dark, null))
                    textSize = 13f
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }
                val amount = TextView(requireContext()).apply {
                    text = FormatUtils.zar(e.amountZar)
                    setTextColor(resources.getColor(R.color.bq_accent, null))
                    textSize = 20f
                }
                row.addView(dayView)
                row.addView(desc)
                row.addView(amount)
                binding.listContainer.addView(row)

                if (index != list.take(50).lastIndex) {
                    val divider = View(requireContext()).apply {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
                        setBackgroundColor(resources.getColor(R.color.bq_input_border, null))
                    }
                    binding.listContainer.addView(divider)
                }
            }
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
