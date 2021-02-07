package edu.fer.unizg.msins.chatty.chats

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onPreShow
import com.afollestad.materialdialogs.customview.customView
import edu.fer.unizg.msins.chatty.R
import edu.fer.unizg.msins.chatty.login.User
import edu.fer.unizg.msins.chatty.chat.ChatFragment
import edu.fer.unizg.msins.chatty.databinding.FragmentMultipleChatsBinding
import edu.fer.unizg.msins.chatty.preferences.ChattyPreferences
import kotlinx.android.synthetic.main.fragment_multiple_chats.*
import kotlinx.android.synthetic.main.fragment_multiple_chats.view.*
import kotlinx.android.synthetic.main.view_search_channel.*

class MultipleChatsFragment : Fragment() {
    private lateinit var binding: FragmentMultipleChatsBinding
    private lateinit var multipleChatsViewModel: MultipleChatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println(User.getCurrent())
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_multiple_chats, container, false
        )

        multipleChatsViewModel =
            ViewModelProvider(this).get(MultipleChatsViewModel::class.java)

        binding.multipleChatsViewModel = multipleChatsViewModel
        binding.lifecycleOwner = this

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar as Toolbar)

        binding.chatPager.adapter = createViewPager2Adapter(multipleChatsViewModel)

        multipleChatsViewModel.eventChatAdded.observe(viewLifecycleOwner, Observer {
            onNewChatAdded()
        })

        binding.let {
            it.chatTabs.attachViewPager2(it.chatPager, multipleChatsViewModel.chats.value)
        }

        binding.chatPager.setPageTransformer(ZoomOutPageTransformer())
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
        inflater.inflate(R.menu.add_new_chat, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.newChatButton -> {
                MaterialDialog(this.context!!).show {
                    onPreShow {
                        search_channel.requestFocus()
                        add_searched_channel.setOnClickListener {
                            multipleChatsViewModel.addNewChat(search_channel.editText?.text.toString())

                            this.cancel()
                        }
                    }
                    customView(R.layout.view_search_channel)
                }
            }
            R.id.login_item, R.id.logout_item -> {
                User.setCurrent(User.ANONYMOUS)
                goToLoginScreen()
            }
            R.id.profile_item -> {
                findNavController().navigate(MultipleChatsFragmentDirections.actionMultipleChatFragmentToProfileFragment())
            }
            else -> {
                throw NotImplementedError()
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        when (User.getCurrent()) {
            User.ANONYMOUS -> {
                menu.findItem(R.id.logout_item).isVisible = false
                menu.findItem(R.id.login_item).isVisible = true
                menu.findItem(R.id.profile_item).isVisible = false
            }
            else -> {
                menu.findItem(R.id.logout_item).isVisible = true
                menu.findItem(R.id.login_item).isVisible = false
                menu.findItem(R.id.profile_item).isVisible = true
            }
        }
    }

    private fun createViewPager2Adapter(chatsViewModel: MultipleChatsViewModel): RecyclerView.Adapter<*> {
        return object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return ChatFragment.create(chatsViewModel.chats.value?.get(position).toString())
            }

            override fun getItemCount(): Int {
                return chatsViewModel.chats.value?.size!!
            }

        }
    }

    private fun onNewChatAdded() {
        binding.let {
            val newTab = it.chatTabs.newTab()
            it.chatTabs.addTab(newTab)
            it.chatPager.adapter?.notifyItemInserted(multipleChatsViewModel.lastChatIndex())
            it.chatPager.currentItem = multipleChatsViewModel.lastChatIndex()
        }
    }

    override fun onResume() {
        if (ChattyPreferences.isFirstRun()) {
            ChattyPreferences.setFirstRun(false)
            goToLoginScreen()
        }
        super.onResume()
    }

    private fun goToLoginScreen() {
        val action = MultipleChatsFragmentDirections.actionMultipleChatFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}