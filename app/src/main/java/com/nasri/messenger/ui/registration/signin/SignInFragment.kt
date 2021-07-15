package com.nasri.messenger.ui.registration.signin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import com.nasri.messenger.R
import com.nasri.messenger.databinding.FragmentSignInBinding
import com.nasri.messenger.domain.result.data
import com.nasri.messenger.domain.result.succeeded
import com.nasri.messenger.domain.user.AuthenticatedUserInfo
import com.nasri.messenger.ui.base.BaseFragment
import timber.log.Timber
import com.nasri.messenger.data.firebase.FirebaseConstants.*
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_COLL_USERS
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_DISPLAY_NAME
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_LAST_SIGN_IN
import com.nasri.messenger.data.firebase.FirebaseConstants.Companion.FIRE_PHOTO_URL


class SignInFragment : BaseFragment() {
    companion object {
        const val GOOGLE_SIGN_IN = 123
    }

    private lateinit var binding: FragmentSignInBinding

    private val viewModel: SignInViewModel by viewModels()

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
        setupSignUpLink()
        clearErrorOnTextChanged()
        setupGoogleSignIn()

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
                viewModel.performEmailSignIn(email, password)
            }

        }

        viewModel.authenticatedUserInfo.observe(viewLifecycleOwner, {
            if (it.succeeded && it != null) {
                preferenceStorage.saveAuthenticatedUser(it.data!!)
                requireActivity().finish()
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

        viewModel.showProgress.observe(viewLifecycleOwner, {
            Timber.d("Context : %s", context.toString())
            if (it) {
                dialogManager.showProgressDialog()
            } else {
                dialogManager.hideProgressDialog()
            }
        })
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
            .setTextColor(Color.WHITE)
            .setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

        binding.dontHaveAccountTextview.applyLinks(signUpLink)
    }


}