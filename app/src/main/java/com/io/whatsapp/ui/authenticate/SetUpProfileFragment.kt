package com.io.whatsapp.ui.authenticate

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.App
import com.io.whatsapp.R
import com.io.whatsapp.databinding.FragmentSetUpProfileBinding
import com.io.whatsapp.ui.invisible
import com.io.whatsapp.ui.visible
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.offline.ChatDomain
import timber.log.Timber

class SetUpProfileFragment : Fragment() {

    private var _binding:FragmentSetUpProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetUpProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        completeProfileButtonClicked()
    }


    private fun completeProfileButtonClicked(){

        with(binding){
            completeProfileButton.setOnClickListener {

                Timber.i("button clicked")
                if (!validateNameEditText()) return@setOnClickListener

                App.saveUserName(binding.userNameEditText.text.toString())

                setUpGetStream()
            }
        }

    }


    private fun validateNameEditText():Boolean{
        with(binding){
            if (userNameEditText.text.isNullOrEmpty()){
                Timber.i("validation failed")
                userNameEditText.apply {
                    error = "Enter a name"
                    requestFocus()
                    return false
                }
            }

        }

        return true
    }



    private fun setUpGetStream(){


        val chatClient = ChatClient.Builder(apiKey = "yek6cvxkzdkt", requireContext())
            .logLevel(ChatLogLevel.ALL)
            .build()
        ChatDomain.Builder(chatClient, requireContext()).build()

        val token = ChatClient.instance().devToken(App.getPhoneNumber().toString())
        val phoneNumber = App.getPhoneNumber().toString()
        val phoneNumberFormatted = phoneNumber.removePrefix("+")

        val user = User(
            id = phoneNumberFormatted,
            extraData = mutableMapOf(
                "name" to App.getUserName().toString()
            )
        )

        val client = ChatClient.instance()

        showProgressbar()

        client.connectUser(user, token).enqueue(){result ->
            if (result.isSuccess){
                Timber.i("chat user connected with ${result.data()}")
                hideProgressBar()
                navigateToHomeFragment()
            }else{
                Timber.i("chat user connection failed with ${result.error()}")
                hideProgressBar()
            }

        }
    }

    private fun showProgressbar(){
        with(binding){
            progressBar.visible()
            completeProfileButton.invisible()
        }
    }

    private fun hideProgressBar(){
        with(binding){
            progressBar.invisible()
            completeProfileButton.visible()
        }
    }


    private fun navigateToHomeFragment(){
        findNavController().navigate(R.id.action_setUpProfileFragment_to_homeFragment)
    }



}