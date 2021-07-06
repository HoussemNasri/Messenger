package com.nasri.messenger.ui.registration.signin

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import com.nasri.messenger.R
import com.nasri.messenger.databinding.FragmentSignInBinding
import com.nasri.messenger.ui.ProgressDialogUtil


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSignUpLink()

        binding.signInButton.setOnClickListener {
            val email = binding.emailTextField.text.toString()
            val password = binding.passwordTextField.text.toString()

            if (email.isBlank()) {
                binding.emailTextField.error = "Email cannot be blank"
            } else if (!isEmailValid(email)) {
                binding.emailTextField.error = "Email is badly formatted"
            }

            if (password.isBlank()) {
                binding.passwordTextField.error = "Password cannot be blank"
            }

            if (email.isNotBlank() && password.isNotBlank() && isEmailValid(email)) {
                ProgressDialogUtil.showProgressDialog(requireContext())
                viewModel.onEmailSignIn(email, password)
            }

        }

        viewModel.userAuthenticated.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
            } else {
                Toast.makeText(
                    this@SignInFragment.context,
                    it.exceptionOrNull()?.message ?: "Unknown Error",
                    Toast.LENGTH_LONG
                ).show()
                resetInputFields()
            }
        })

        viewModel.dismissProgressDialogAction.observe(viewLifecycleOwner, {
            if (!it.hasBeenHandled) {
                ProgressDialogUtil.dismiss()
            }
        })

    }

    private fun resetInputFields() {
        binding.emailTextField.setText("")
        binding.passwordTextField.setText("")
        binding.passwordTextField.clearFocus()
        binding.emailTextField.clearFocus()
    }

    private fun isEmailValid(target: String): Boolean {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    override fun onDestroy() {
        super.onDestroy()
        ProgressDialogUtil.dismiss()
    }


    private fun setupSignUpLink() {
        val signUpLink = Link(getString(R.string.sign_up_label))
            .setBold(true)
            .setUnderlined(false)
            .setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

        binding.dontHaveAccountTextview.applyLinks(signUpLink)
    }


}