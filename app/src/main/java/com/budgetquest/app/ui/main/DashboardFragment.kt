package com.budgetquest.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.R
import com.budgetquest.app.databinding.FragmentDashboardBinding
import com.budgetquest.app.ui.AppViewModel
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
                binding.welcome.text = "Welcome back, ${user.username}! 🔥"
                binding.levelBadge.text = "Lv ${user.level}"
            }
        }
        val now = LocalDate.now()
        val ym = YearMonth.from(now)
        val daysRemaining = ym.lengthOfMonth() - now.dayOfMonth
        binding.subtitle.text = "${now.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} • $daysRemaining days remaining"
        binding.fab.setOnClickListener { findNavController().navigate(R.id.action_home_to_add_edit) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
