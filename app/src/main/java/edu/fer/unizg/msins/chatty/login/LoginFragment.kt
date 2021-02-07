package edu.fer.unizg.msins.chatty.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.fer.unizg.msins.chatty.R
import edu.fer.unizg.msins.chatty.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.eventTwitchLogin.observe(viewLifecycleOwner, Observer {
            onTwitchLogIn()
        })

        loginViewModel.eventAnonymousLogin.observe(viewLifecycleOwner, Observer {
            onAnonymousLogIn()
        })

        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun onTwitchLogIn() {
        val action = LoginFragmentDirections.actionLoginFragmentToTwitchLoginFragment()
        findNavController().navigate(action)
    }

    private fun onAnonymousLogIn() {
        User.setCurrent(User.ANONYMOUS)
        val action = LoginFragmentDirections.actionLoginFragmentToMultipleChatFragment()
        findNavController().navigate(action)
    }
}