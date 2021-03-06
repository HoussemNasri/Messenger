package com.nasri.messenger.ui.auth.signin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import com.nasri.messenger.R
import com.nasri.messenger.data.SharedPreferenceStorage
import com.nasri.messenger.data.user.FirebaseAuthRepository
import com.nasri.messenger.data.user.FirebaseUserService
import com.nasri.messenger.data.user.UserRepositoryImpl
import com.nasri.messenger.databinding.FragmentSignInBinding
import com.nasri.messenger.domain.auth.signin.SignInUseCase
import com.nasri.messenger.domain.inputvalidation.EmailVerifier
import com.nasri.messenger.domain.inputvalidation.PasswordVerifier
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.ui.base.BaseFragment
import timber.log.Timber


class SignInFragment : BaseFragment() {
    companion object {
        const val GOOGLE_SIGN_IN = 123
    }

    private lateinit var binding: FragmentSignInBinding

    private val viewModel: SignInViewModel by viewModels {
        val auth = FirebaseAuth.getInstance()
        val userService = FirebaseUserService(FirebaseFirestore.getInstance())
        val userRepository = UserRepositoryImpl(userService)
        val authRepository = FirebaseAuthRepository(
            auth, userRepository, SharedPreferenceStorage(requireContext())
        )
        SignInViewModelFactory(SignInUseCase(authRepository))
    }

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupForgotPasswordLink()
        clearErrorOnTextChanged()
        setupGoogleSignIn()
        hideThirdPartySignInProvidersLayoutOnSoftKeyboardAppear()

        binding.signInButton.setOnClickListener {
            val email = binding.emailTextField.editText?.text.toString().trim()
            val password = binding.passwordTextField.editText?.text.toString().trim()
            val emailFlaws = EmailVerifier().verify(email)
            val passwordFlaws = PasswordVerifier().verify(password)

            if (emailFlaws.isNotEmpty()) {
                binding.emailTextField.error = emailFlaws[0].description
                return@setOnClickListener
            }

            if (passwordFlaws.isNotEmpty()) {
                binding.passwordTextField.error = passwordFlaws[0].description
                return@setOnClickListener
            }

            viewModel.performEmailSignIn(email, password)
        }

        viewModel.userSignedInEvent.observe(viewLifecycleOwner, {
            dialogManager.hideProgressDialog()
            when (it) {
                is Result.Success -> {
                    requireActivity().finish()
                    findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
                }
                is Result.Error -> {
                    Toast.makeText(
                        this@SignInFragment.context,
                        it.exceptionOrNull()?.message ?: "Unknown Error",
                        Toast.LENGTH_LONG
                    ).show()
                    resetInputFields()
                }
                is Result.Loading -> dialogManager.showProgressDialog()
            }
        })

        binding.goToSignUpButton.setOnClickListener {
            resetInputFields()
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun hideThirdPartySignInProvidersLayoutOnSoftKeyboardAppear() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            // if more than 200 dp, it's probably a keyboard...
            Timber.d("Hello : %d", binding.root.rootView.height - binding.root.height)
            if (binding.root.rootView.height - binding.root.height > dpToPx(200)) {
                Timber.d("Keyboard!")
                binding.thirdPartyLayout.visibility = View.GONE
            } else {
                if (binding.thirdPartyLayout.visibility == View.GONE) {
                    binding.thirdPartyLayout.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setupGoogleSignIn() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        binding.signInWithGoogle.setOnClickListener {
            Timber.d(GoogleSignIn.getLastSignedInAccount(requireContext())?.displayName ?: "*")
            startActivityForResult(googleSignInClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == GOOGLE_SIGN_IN) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                if (task.isSuccessful) {
                    val account = task.result!!
                    viewModel.performCredentialSignIn(
                        GoogleAuthProvider.getCredential(
                            account.idToken,
                            null
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Google sign in failed!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
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
        binding.emailTextField.error = null
        binding.passwordTextField.error = null
        binding.passwordTextField.clearFocus()
        binding.emailTextField.clearFocus()
    }

    private fun setupForgotPasswordLink() {
        val signUpLink = Link(getString(R.string.forgot_your_password))
            .setBold(true)
            .setUnderlined(false)
            .setOnClickListener {
                //TODO ('Go to forgot password fragment')
            }

        binding.forgotPasswordTextView.applyLinks(signUpLink)
    }

    fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }


}