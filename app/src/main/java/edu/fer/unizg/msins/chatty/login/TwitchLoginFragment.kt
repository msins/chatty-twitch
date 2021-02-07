package edu.fer.unizg.msins.chatty.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.fer.unizg.msins.chatty.R
import edu.fer.unizg.msins.chatty.databinding.FragmentTwitchLoginBinding
import edu.fer.unizg.msins.chatty.networking.API
import edu.fer.unizg.msins.chatty.preferences.ChattyPreferences
import kotlinx.android.synthetic.main.fragment_twitch_login.view.webview
import timber.log.Timber

class TwitchLoginFragment : Fragment() {
    private lateinit var binding: FragmentTwitchLoginBinding
    private lateinit var viewModel: TwitchLoginViewModel

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_twitch_login, container, false)

        viewModel = ViewModelProvider(this).get(TwitchLoginViewModel::class.java)

        binding.webview.apply {
            with(settings) {
                javaScriptEnabled = true
                setSupportZoom(true)
            }
            CookieManager.getInstance().removeAllCookies(null)
            clearCache(true)
            clearFormData()
            webViewClient = TwitchWebClient(
                onFinish = { _, _ ->
                    if (webview.progress == 100) {
                        binding.loginPlaceholderAnimation.visibility = View.GONE
                        binding.webview.visibility = View.VISIBLE
                    }
                }
            )
            this.loadUrl(API.TWITCH)
        }


        viewModel.eventUserVerificationStarted.observe(viewLifecycleOwner, Observer {
            binding.webview.visibility = View.GONE
            binding.loginPlaceholderAnimation.visibility = View.VISIBLE
            binding.webview.removeAllViews()
            binding.webview.destroy()
        })

        viewModel.eventUserVerified.observe(viewLifecycleOwner, Observer { user ->
            User.setCurrent(user)
            ChattyPreferences.setCurrentUser(user)
            findNavController().navigate(TwitchLoginFragmentDirections.actionTwitchLoginFragmentToMultipleChatFragment())
        })

        showBackButton()
        return binding.root
    }

    private fun showBackButton() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarLogin as Toolbar)
        (binding.toolbarLogin as Toolbar).apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                val action =
                    TwitchLoginFragmentDirections.actionTwitchLoginFragmentToLoginFragment()
                findNavController().navigate(action)
            }
            title = getString(R.string.log_in)
        }
    }

    private inner class TwitchWebClient(val onFinish: (WebView?, String?) -> Unit) :
        WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            onFinish(view, url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            val message = error?.description ?: return
            val code = error.errorCode
            Timber.e("Error $code in WebView: $message")
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(API.BASE)) {
                if (binding.webview.visibility == View.GONE) {
                    binding.webview.visibility = View.VISIBLE
                }
            }

            if (request?.url.toString() == "https://www.twitch.tv/?no-reload=true") {
                binding.webview.visibility = View.GONE
                binding.webview.removeAllViews()
                binding.webview.loadUrl(API.TWITCH)
                return false
            }

            if (request?.url.toString().startsWith("http://localhost/#access_token=")) {
                val fragment = request?.url?.fragment ?: return false
                val regex = "access_token=([^&]+)".toRegex()
                val token = regex.find(fragment)!!.destructured
                val oAuth = token.component1()
                Timber.d("OAUTH: ${User.getCurrent().oAuth}")
                viewModel.verifyUser(oAuth)
                return false
            }
            return false
        }
    }

}

