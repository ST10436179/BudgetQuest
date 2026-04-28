package com.budgetquest.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.data.db.CategoryEntity
import com.budgetquest.app.databinding.FragmentCategoryManagerBinding
import com.budgetquest.app.ui.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Category tile editor with add/edit/delete interactions.
 */
class CategoryManagerFragment : Fragment() {
    private var _binding: FragmentCategoryManagerBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.addTile.setOnClickListener { showAddDialog() }
        vm.categoriesLive().observe(viewLifecycleOwner) { cats ->
            binding.tileContainer.removeAllViews()
            cats.forEach { cat ->
                val tile = TextView(requireContext()).apply {
                    text = "${cat.emoji} ${cat.name}"
                    setPadding(16, 16, 16, 16)
                    setBackgroundColor(android.graphics.Color.parseColor(cat.colorHex))
                    setTextColor(android.graphics.Color.WHITE)
                    setOnClickListener { showEditDialog(cat) }
                    setOnLongClickListener { showDeleteDialog(cat); true }
                }
                binding.tileContainer.addView(tile)
            }
        }
    }

    private fun showAddDialog() {
        val names = arrayOf("Travel ✈️", "Gym 🏋️", "Pets 🐶", "Savings 💰")
        AlertDialog.Builder(requireContext())
            .setTitle("Choose preset category")
            .setItems(names) { _, which ->
                val selected = names[which].split(" ")
                val userId = vm.currentUserId.value ?: return@setItems
                CoroutineScope(Dispatchers.IO).launch {
                    com.budgetquest.app.data.db.AppDatabase.getInstance(requireContext()).categoryDao().insert(
                        CategoryEntity(userId = userId, name = selected[0], emoji = selected[1], colorHex = "#2A5E38")
                    )
                }
            }.show()
    }

    private fun showEditDialog(cat: CategoryEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Edit ${cat.name}")
            .setMessage("Use preset update to append *")
            .setPositiveButton("Apply") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    com.budgetquest.app.data.db.AppDatabase.getInstance(requireContext()).categoryDao().update(cat.copy(name = "${cat.name}*"))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(cat: CategoryEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete ${cat.name}?")
            .setMessage("Warning: expenses in this category may be affected.")
            .setPositiveButton("Delete") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    com.budgetquest.app.data.db.AppDatabase.getInstance(requireContext()).categoryDao().delete(cat)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
