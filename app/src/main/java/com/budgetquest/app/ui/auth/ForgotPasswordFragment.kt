package com.budgetquest.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.budgetquest.app.databinding.FragmentForgotPasswordBinding
import com.budgetquest.app.ui.AppViewModel
import com.budgetquest.app.util.SecurityUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Simple security question based reset flow.
 */
class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.resetBtn.setOnClickListener {
            binding.errorText.text = ""
            val username = binding.username.text?.toString().orEmpty()
            val answer = binding.answer.text?.toString().orEmpty()
            val newPassword = binding.newPassword.text?.toString().orEmpty()
            if (username.isBlank() || answer.isBlank() || newPassword.isBlank()) {
                binding.errorText.text = "All fields are required"
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                val user = com.budgetquest.app.data.db.AppDatabase.getInstance(requireContext()).userDao().findByUsername(username)
                if (user == null) {
                    withContext(Dispatchers.Main) { binding.errorText.text = "User not found" }
                } else if (!user.securityAnswer.equals(answer, true)) {
                    withContext(Dispatchers.Main) { binding.errorText.text = "Security answer does not match" }
                } else {
                    com.budgetquest.app.data.db.AppDatabase.getInstance(requireContext()).userDao().update(
                        user.copy(passwordHash = SecurityUtils.sha256(newPassword))
                    )
                    withContext(Dispatchers.Main) { binding.errorText.text = "Password updated. Return to login." }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
