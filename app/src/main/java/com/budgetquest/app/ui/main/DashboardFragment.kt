package com.budgetquest.app.ui.main

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
import com.budgetquest.app.databinding.FragmentDashboardBinding
import com.budgetquest.app.ui.AppViewModel
import com.budgetquest.app.util.FormatUtils
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * Home screen showing greeting, level, month context and add-expense entry.
 */
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.refreshUser()
        vm.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.welcome.text = "${user.username} 👋"
                binding.levelBadge.text = "Lv ${user.level}"
            }
        }
        val now = LocalDate.now()
        val ym = YearMonth.from(now)
        val daysRemaining = ym.lengthOfMonth() - now.dayOfMonth
        binding.subtitle.text = "${now.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} • $daysRemaining days remaining"
        bindSummaryCards()
        bindRecentExpenses()
        binding.fab.setOnClickListener { findNavController().navigate(R.id.action_home_to_add_edit) }
    }

    private fun bindSummaryCards() {
        val start = LocalDate.now().withDayOfMonth(1).toString()
        val end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString()
        vm.expensesLive(start, end).observe(viewLifecycleOwner) { list ->
            val total = list.sumOf { it.amountZar }
            binding.gaugeText.text = FormatUtils.zar(total)
            binding.gauge.progress = ((total / 5000.0) * 100).toInt().coerceIn(0, 100)
            binding.statusCard1.text = "🎯\nOn Track"
            binding.statusCard2.text = "📁\n6"
            binding.statusCard3.text = "🧾\n${list.size}"
            binding.statusCard4.text = "⚠️\n1 Over"
        }
    }

    private fun bindRecentExpenses() {
        val start = LocalDate.now().withDayOfMonth(1).toString()
        val end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString()
        vm.expensesLive(start, end).observe(viewLifecycleOwner) { list ->
            binding.recentListContainer.removeAllViews()
            list.take(3).forEach { e ->
                val row = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(8, 10, 8, 10)
                }
                val left = TextView(requireContext()).apply {
                    text = "🛒  ${e.description}\n${e.date}"
                    setTextColor(resources.getColor(R.color.bq_text_dark, null))
                    textSize = 14f
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                }
                val right = TextView(requireContext()).apply {
                    text = FormatUtils.zar(e.amountZar)
                    setTextColor(resources.getColor(R.color.bq_accent, null))
                    textSize = 16f
                }
                row.addView(left)
                row.addView(right)
                row.setOnClickListener {
                    val bundle = Bundle().apply { putString("expenseId", e.id.toString()) }
                    findNavController().navigate(R.id.addEditExpenseFragment, bundle)
                }
                binding.recentListContainer.addView(row)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
