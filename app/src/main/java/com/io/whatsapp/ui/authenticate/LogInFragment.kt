package com.io.whatsapp.ui.authenticate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.App
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.io.whatsapp.R
import com.io.whatsapp.databinding.FragmentLogInBinding
import com.io.whatsapp.ui.invisible
import com.io.whatsapp.ui.visible
import kotlinx.android.synthetic.main.fragment_log_in.*
import timber.log.Timber
import java.util.concurrent.TimeUnit


class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var authCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        sendOtpButtonClicked()
    }


    private fun sendOtpButtonClicked() {
        binding.sendOtpButton.setOnClickListener {

            if (!validatePhoneNumberLength()) return@setOnClickListener

            val phoneNumber = "+234${enter_phone_number_edit_text.text.toString()}"
            sendOtp(phoneNumber)
        }

    }


    private fun sendOtp(phoneNumber: String) {
        showOtpProgressBar()
        authCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Timber.i("verification complete")
                hideOtpProgressBar()

                val phoneNumberId = phoneNumber.removePrefix("+")
                signInWithPhoneAuthCredential(credential, phoneNumberId)
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Timber.i("verification failed due to $exception")
                hideOtpProgressBar()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                hideOtpProgressBar()
                storedVerificationId = verificationId
                resendToken = token
            }

        }

        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(authCallBacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        Timber.i("auth started")
    }

    private fun validateOtpLength(): Boolean {
        with(binding) {

            if (otpEditText.text?.count()!! < 4) {
                otpEditText.apply {
                    error = "Invalid otp"
                    requestFocus()
                }
                return false
            }
        }

        return true
    }

    private fun validatePhoneNumberLength(): Boolean {
        binding.let {
            if (enter_phone_number_edit_text.text?.length!! < 10) {
                enter_phone_number_edit_text.apply {
                    error = "Please enter your phone number excluding the first zero"
                    requestFocus()
                    return false
                }
            }
        }

        return true
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        phoneNumber: String
    ) {
        binding.signInButton.setOnClickListener {
            if (!validateOtpLength()) return@setOnClickListener
            showSignInProgressBar()
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.i("sign in complete")
                    hideSignInProgressBar()
                    val user = task.result?.user


                    user?.let {
                        App.apply {
                            saveUid(it.uid)
                            savePhoneNumber(phoneNumber)
                        }

                        navigateToSetUpProfileFragment()
                    }

                } else {
                    hideSignInProgressBar()
                    Timber.i("sign in failed due to ${task.exception}")
                }

            }
        }
    }

    private fun showSignInProgressBar() {
        with(binding) {
            signInProgressBar.visible()
            signInButton.invisible()
        }
    }

    private fun hideSignInProgressBar() {
        with(binding) {
            signInProgressBar.invisible()
            signInButton.visible()
        }
    }


    private fun showOtpProgressBar() {
        with(binding) {
            otpProgressBar.visible()
            sendOtpButton.invisible()
        }
    }

    private fun hideOtpProgressBar() {
        with(binding) {
            otpProgressBar.invisible()
            sendOtpButton.visible()
        }
    }

    private fun navigateToSetUpProfileFragment() {
        findNavController().navigate(R.id.action_logInOrSignUpFragment_to_setUpProfileFragment)
    }


}