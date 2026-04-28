package com.budgetquest.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.R
import com.budgetquest.app.databinding.FragmentRegisterBinding
import com.budgetquest.app.ui.AppViewModel

/**
 * Registration flow with inline field validation and default category seeding.
 */
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val questions = listOf("What is your first pet's name?", "What city were you born in?", "What is your favorite teacher's name?")
        binding.securityQuestion.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, questions)
        vm.authError.observe(viewLifecycleOwner) { binding.generalError.text = it.orEmpty() }

        binding.registerBtn.setOnClickListener {
            clearErrors()
            val username = binding.username.text?.toString().orEmpty()
            val password = binding.password.text?.toString().orEmpty()
            val confirm = binding.confirmPassword.text?.toString().orEmpty()
            val answer = binding.securityAnswer.text?.toString().orEmpty()
            val question = binding.securityQuestion.selectedItem?.toString().orEmpty()
            var valid = true
            if (username.length < 4) {
                binding.usernameError.text = "Username must be at least 4 characters"
                valid = false
            }
            if (!Regex("^(?=.*[A-Z])(?=.*\\d).{8,}$").matches(password)) {
                binding.passwordError.text = "Password must be 8+ chars with number and uppercase"
                valid = false
            }
            if (confirm != password) {
                binding.confirmError.text = "Passwords do not match"
                valid = false
            }
            if (answer.isBlank()) {
                binding.answerError.text = "Security answer is required"
                valid = false
            }
            if (!valid) return@setOnClickListener

            vm.register(username, password, question, answer) {
                findNavController().navigate(R.id.action_register_to_home)
            }
        }
    }

    private fun clearErrors() {
        binding.usernameError.text = ""
        binding.passwordError.text = ""
        binding.confirmError.text = ""
        binding.answerError.text = ""
        binding.generalError.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
