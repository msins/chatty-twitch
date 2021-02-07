package edu.fer.unizg.msins.chatty.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.fer.unizg.msins.chatty.R
import edu.fer.unizg.msins.chatty.login.User
import edu.fer.unizg.msins.chatty.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var viewModel: FollowingViewModel
    private lateinit var followersAdapter: StringRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_following, container, false
        )

        viewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)

        followersAdapter = StringRecyclerAdapter(mutableListOf())
        binding.followingList.apply {
            layoutManager = LinearLayoutManager(this@FollowingFragment.activity)
            this.adapter = followersAdapter
        }

        viewModel.eventFollowsFetched.observe(viewLifecycleOwner, Observer { list ->
            followersAdapter.updateData(list)
        })

        viewModel.fetchUserFollows(User.getCurrent().oAuth, User.getCurrent().id)

        return binding.root
    }
}