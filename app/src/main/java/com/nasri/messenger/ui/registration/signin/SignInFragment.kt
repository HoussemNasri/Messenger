package com.nasri.messenger.ui.registration.signin

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import com.nasri.messenger.R
import com.nasri.messenger.databinding.FragmentSignInBinding
import com.nasri.messenger.domain.result.data
import com.nasri.messenger.domain.result.succeeded
import com.nasri.messenger.ui.base.BaseFragment
import com.nasri.messenger.ui.ProgressDialogUtil


class SignInFragment : BaseFragment() {
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
        clearErrorOnTextChanged()

        binding.signInButton.setOnClickListener {
            val email = binding.emailTextField.editText?.text.toString()
            val password = binding.passwordTextField.editText?.text.toString()

            if (email.isBlank()) {
                binding.emailTextField.error = getString(R.string.error_email_blank)
            } else if (!isEmailValid(email)) {
                binding.emailTextField.error = getString(R.string.error_email_formatting)
            }

            if (password.isBlank()) {
                binding.passwordTextField.error = getString(R.string.error_password_blank)
            }

            if (email.isNotBlank() && password.isNotBlank() && isEmailValid(email)) {
                showProgress()
                viewModel.onEmailSignIn(email, password)
            }

        }

        viewModel.authenticatedUserInfo.observe(viewLifecycleOwner, {
            if (it.succeeded && it != null) {
                // TODO (Save user info in SharedPreferences)
                preferenceStorage.saveAuthenticatedUser(it.data!!)
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

    private fun clearErrorOnTextChanged() {
        binding.emailTextField.editText?.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.emailTextField.error = null
            }
        }

        binding.passwordTextField.editText?.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.passwordTextField.error = null
            }
        }
    }

    private fun resetInputFields() {
        binding.emailTextField.editText?.setText("")
        binding.passwordTextField.editText?.setText("")
        binding.passwordTextField.clearFocus()
        binding.emailTextField.clearFocus()
    }

    private fun isEmailValid(target: String): Boolean {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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