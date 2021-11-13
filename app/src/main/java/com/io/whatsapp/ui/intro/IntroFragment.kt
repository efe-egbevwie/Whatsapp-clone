package com.io.whatsapp.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.io.whatsapp.R
import com.io.whatsapp.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private var _binding:FragmentIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIntroBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToSignInFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun navigateToSignInFragment(){
        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_logInOrSignUpFragment)
        }
    }


}