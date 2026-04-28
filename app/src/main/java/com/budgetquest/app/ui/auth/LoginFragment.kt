package com.budgetquest.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budgetquest.app.R
import com.budgetquest.app.databinding.FragmentLoginBinding
import com.budgetquest.app.ui.AppViewModel

/**
 * Login screen with inline validation and no Toast error usage.
 */
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val vm by activityViewModels<AppViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vm.attemptAutoLogin()
        vm.currentUserId.observe(viewLifecycleOwner) {
            if (it != null && it > 0) findNavController().navigate(R.id.action_login_to_home)
        }
        vm.authError.observe(viewLifecycleOwner) { binding.errorText.text = it.orEmpty() }

        binding.username.doAfterTextChanged { binding.errorText.text = "" }
        binding.password.doAfterTextChanged { binding.errorText.text = "" }
        binding.loginBtn.setOnClickListener {
            try {
                val username = binding.username.text?.toString().orEmpty()
                val password = binding.password.text?.toString().orEmpty()
                if (username.isBlank() || password.isBlank()) {
                    binding.errorText.text = "Username and password are required"
                } else {
                    vm.login(username, password)
                }
            } catch (_: Exception) {
                binding.errorText.text = "Invalid input"
            }
        }
        binding.demoBtn.setOnClickListener {
            vm.enableDemoAutoLogin()
            binding.username.setText(AppViewModel.DEMO_USERNAME)
            binding.password.setText(AppViewModel.DEMO_PASSWORD)
            vm.login(AppViewModel.DEMO_USERNAME, AppViewModel.DEMO_PASSWORD)
        }
        binding.createAccount.setOnClickListener { findNavController().navigate(R.id.action_login_to_register) }
        binding.forgotPassword.setOnClickListener { findNavController().navigate(R.id.action_login_to_forgot) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
