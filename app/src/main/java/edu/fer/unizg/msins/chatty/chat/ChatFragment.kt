package edu.fer.unizg.msins.chatty.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import edu.fer.unizg.msins.chatty.R
import edu.fer.unizg.msins.chatty.login.User
import edu.fer.unizg.msins.chatty.databinding.FragmentChatBinding
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_input.*
import kotlinx.android.synthetic.main.fragment_input.view.*
import org.pircbotx.hooks.events.OutputEvent
import org.pircbotx.hooks.types.GenericMessageEvent


class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var fastAdapter: FastAdapter<ChatMessage>
    private lateinit var itemAdapter: ItemAdapter<ChatMessage>
    private var automaticScroll: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_chat, container, false
        )

        val channelName = arguments?.getString("channel")
        val channel = TwitchChannel(
            channelName!!, TwitchListenerAdapter(
                onConnect = this::onConnect,
                onNewMessage = this::onNewMessage,
                onSentMessage = this::onSentMessage
            )
        )

        val chatViewModelFactory = ChatViewModelFactory(channel)
        chatViewModel = ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel::class.java)

        chatViewModel.historyUpdates.observe(viewLifecycleOwner, Observer { history ->
            if (history != null) setData(history)
            if (automaticScroll) {
                binding.chatRecyclerView.scrollToPosition(itemAdapter.adapterItemCount - 1)
            }
        })

//        chatViewModel.sendMessageEvent.observe(viewLifecycleOwner, Observer { message ->
//        })

        itemAdapter = ItemAdapter.items()
        fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.itemCount

        binding.scrollDown.setOnClickListener {
            automaticScroll = true
            binding.chatRecyclerView.scrollToPosition(itemAdapter.adapterItemCount - 1)
            scroll_down.hide()
        }

        binding.chatRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    automaticScroll = false
                    scroll_down.show()
                } else if (!recyclerView.canScrollVertically(1) && scroll_down.visibility == View.VISIBLE) {
                    scroll_down.hide()
                }

            }
        })
        fastAdapter.setHasStableIds(true)
        binding.chatRecyclerView.setItemViewCacheSize(40)
        binding.chatRecyclerView.isNestedScrollingEnabled = false
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.chatRecyclerView.itemAnimator = null
        binding.chatRecyclerView.setHasFixedSize(true)
        binding.chatRecyclerView.adapter = fastAdapter

        binding.let {
            if (User.getCurrent() != User.ANONYMOUS) {
                it.chatInput.visibility = View.VISIBLE
                it.chatInput.send_button.setOnClickListener {
                    chatViewModel.sendMessage(input_edittext.text.toString())
                    input_edittext.text?.clear()
                }
            }
        }

        chatViewModel.startDownloadingEmotes()

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        chatViewModel.startIrcBot()
    }

    private fun setData(items: List<ChatMessage>) {
        FastAdapterDiffUtil[itemAdapter] = items
    }

    companion object {
        fun create(channel: String): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle(1).apply {
                    putString("channel", channel)
                }
            }
        }
    }

    private fun onConnect() {
        activity!!.runOnUiThread {
            chat_recycler_view.visibility = View.VISIBLE
            chat_placeholder_animation.clearAnimation()
            chat_placeholder_animation.visibility = View.GONE
        }
    }

    private fun onSentMessage(event: OutputEvent?) {

    }

    private fun onNewMessage(event: GenericMessageEvent?) {
        chatViewModel.addMessage(event)
    }
}