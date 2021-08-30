package com.nasri.messenger.ui.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.data.SharedPreferenceStorage
import com.nasri.messenger.data.user.FirebaseAuthRepository
import com.nasri.messenger.data.user.FirebaseUserService
import com.nasri.messenger.data.user.UserRepositoryImpl
import com.nasri.messenger.databinding.FragmentSignUpBinding
import com.nasri.messenger.domain.auth.signin.SignInUseCase
import com.nasri.messenger.domain.auth.signup.SignUpUseCase
import com.nasri.messenger.domain.inputvalidation.EmailVerifier
import com.nasri.messenger.domain.inputvalidation.PasswordVerifier
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.result.succeeded
import com.nasri.messenger.ui.auth.signin.SignInViewModelFactory
import com.nasri.messenger.ui.base.BaseFragment
import timber.log.Timber


class SignUpFragment : BaseFragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels {
        val auth = FirebaseAuth.getInstance()
        val userService = FirebaseUserService(FirebaseFirestore.getInstance())
        val userRepository = UserRepositoryImpl(userService)
        val authRepository = FirebaseAuthRepository(
            auth, userRepository, SharedPreferenceStorage(requireContext())
        )
        SignUpViewModelFactory(SignUpUseCase(authRepository))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clearErrorOnTextChanged()

        binding.signupButton.setOnClickListener {
            val email = binding.signupEmailTextField.editText?.text.toString()
            val password = binding.signupPasswordTextField.editText?.text.toString()
            val confirmPassword = binding.signupConfirmTextField.editText?.text.toString()

            val emailFlaws = EmailVerifier().verify(email)
            val passwordFlaws = PasswordVerifier().verify(password)

            if (emailFlaws.isNotEmpty()) {
                binding.signupEmailTextField.error = emailFlaws[0].description
                return@setOnClickListener
            }

            if (passwordFlaws.isNotEmpty()) {
                binding.signupPasswordTextField.error = passwordFlaws[0].description
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.signupConfirmTextField.error = "The password confirmation does not match"
                return@setOnClickListener
            }

            viewModel.signUpWithEmailAndPassword(email, password)

        }

        viewModel.userSignedUpEvent.observe(viewLifecycleOwner, {
            dialogManager.hideProgressDialog()
            when (it) {
                is Result.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "User Registered successfully",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    findNavController().navigateUp()
                }
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.exceptionOrNull()?.message ?: "Unknown error!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Result.Loading -> {
                    dialogManager.showProgressDialog()
                }
            }
        })
    }

    private fun clearErrorOnTextChanged() {
        binding.signupEmailTextField.editText?.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.signupEmailTextField.error = null
            }
        }

        binding.signupPasswordTextField.editText?.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.signupPasswordTextField.error = null
            }
        }

        binding.signupConfirmTextField.editText?.addTextChangedListener {
            if (it?.length ?: 0 > 0) {
                binding.signupConfirmTextField.error = null
            }
        }
    }

    private fun resetInputFields() {
        binding.signupEmailTextField.editText?.setText("")
        binding.signupEmailTextField.error = null
        binding.signupEmailTextField.clearFocus()

        binding.signupPasswordTextField.editText?.setText("")
        binding.signupPasswordTextField.error = null
        binding.signupPasswordTextField.clearFocus()
    }


}