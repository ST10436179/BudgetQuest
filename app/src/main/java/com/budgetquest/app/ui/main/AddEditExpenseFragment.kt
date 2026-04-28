package com.budgetquest.app.ui.main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.data.db.CategoryEntity
import com.budgetquest.app.data.db.ExpenseEntity
import com.budgetquest.app.databinding.FragmentAddEditExpenseBinding
import com.budgetquest.app.ui.AppViewModel
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Full-screen expense creation/editing with camera/gallery receipt support.
 */
class AddEditExpenseFragment : Fragment() {
    private var _binding: FragmentAddEditExpenseBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()
    private var currentPhotoPath: String? = null
    private var selectedCategories: List<CategoryEntity> = emptyList()
    private var editExpenseId: Long = -1L

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && currentPhotoPath != null) {
            binding.receiptPreview.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath))
        }
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            currentPhotoPath = copyToInternal(uri, requireContext().contentResolver)
            binding.receiptPreview.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editExpenseId = arguments?.getString("expenseId")?.toLongOrNull() ?: -1L
        if (editExpenseId > 0) binding.title.text = "Edit Expense"
        prefillDefaultsForNewExpense()
        bindPickers()
        bindCategorySpinner()
        binding.receiptBox.setOnClickListener { showReceiptPickerDialog() }
        binding.saveBtn.setOnClickListener { saveExpense() }
        if (editExpenseId > 0) loadExpenseForEdit(editExpenseId)
    }

    private fun prefillDefaultsForNewExpense() {
        if (editExpenseId > 0) return
        if (binding.dateInput.text.isNullOrBlank()) binding.dateInput.setText(LocalDate.now().toString())
        if (binding.startTime.text.isNullOrBlank()) binding.startTime.setText("09:00")
        if (binding.endTime.text.isNullOrBlank()) binding.endTime.setText("09:30")
    }

    private fun bindPickers() {
        binding.dateInput.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                binding.dateInput.setText("%04d-%02d-%02d".format(y, m + 1, d))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.startTime.setOnClickListener { showTimePicker(binding.startTime) }
        binding.endTime.setOnClickListener { showTimePicker(binding.endTime) }
    }

    private fun showTimePicker(target: android.widget.EditText) {
        val c = Calendar.getInstance()
        TimePickerDialog(requireContext(), { _, h, m ->
            target.setText("%02d:%02d".format(h, m))
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }

    private fun bindCategorySpinner() {
        vm.categoriesLive().observe(viewLifecycleOwner) {
            selectedCategories = it
            binding.categorySpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it.map { c -> "${c.emoji} ${c.name}" })
        }
    }

    private fun showReceiptPickerDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Attach receipt")
            .setItems(arrayOf("Camera", "Gallery")) { _, which ->
                if (which == 0) launchCamera() else galleryLauncher.launch("image/*")
            }.show()
    }

    private fun launchCamera() {
        val file = File(requireContext().filesDir, "receipt_${System.currentTimeMillis()}.jpg")
        currentPhotoPath = file.absolutePath
        val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)
        cameraLauncher.launch(uri)
    }

    private fun copyToInternal(uri: Uri, resolver: ContentResolver): String {
        val file = File(requireContext().filesDir, "receipt_gallery_${System.currentTimeMillis()}.jpg")
        resolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        return file.absolutePath
    }

    private fun saveExpense() {
        try {
            val userId = vm.currentUserId.value ?: -1L
            val amount = binding.amountInput.text?.toString()
                ?.replace("R", "", ignoreCase = true)
                ?.replace(",", "")
                ?.trim()
                ?.toDoubleOrNull()
            val date = binding.dateInput.text?.toString().orEmpty()
            val start = binding.startTime.text?.toString().orEmpty()
            val end = binding.endTime.text?.toString().orEmpty()
            val desc = binding.descriptionInput.text?.toString().orEmpty()
            val category = selectedCategories.getOrNull(binding.categorySpinner.selectedItemPosition)

            if (userId <= 0 || amount == null || date.isBlank() || start.isBlank() || end.isBlank() || category == null) {
                binding.errorText.text = if (category == null) {
                    "Please create/select a category first."
                } else {
                    "Please complete all required fields"
                }
                return
            }
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            val expense = ExpenseEntity(
                id = if (editExpenseId > 0) editExpenseId else 0,
                userId = userId,
                categoryId = category.id,
                amountZar = amount,
                date = date,
                startTime = start,
                endTime = end,
                description = desc,
                receiptPhotoPath = currentPhotoPath
            )
            vm.saveExpense(expense, editExpenseId > 0) { findNavController().popBackStack() }
        } catch (e: Exception) {
            Timber.e(e, "saveExpense input error")
            binding.errorText.text = "Invalid input. Please check values."
        }
    }

    private fun loadExpenseForEdit(expenseId: Long) {
        vm.getExpense(expenseId) { exp ->
            if (exp != null) {
                binding.amountInput.setText(exp.amountZar.toString())
                binding.dateInput.setText(exp.date)
                binding.startTime.setText(exp.startTime)
                binding.endTime.setText(exp.endTime)
                binding.descriptionInput.setText(exp.description)
                currentPhotoPath = exp.receiptPhotoPath
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
