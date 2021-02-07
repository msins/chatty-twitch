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
import edu.fer.unizg.msins.chatty.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var viewModel: FollowersViewModel
    private lateinit var followersAdapter: StringRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_followers, container, false
        )

        viewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)

        followersAdapter = StringRecyclerAdapter(mutableListOf())
        binding.followersList.apply {
            layoutManager = LinearLayoutManager(this@FollowersFragment.activity)
            this.adapter = followersAdapter
        }

        viewModel.eventFollowersFetched.observe(viewLifecycleOwner, Observer { list ->
            followersAdapter.updateData(list)
        })

        viewModel.fetchFollowers(User.getCurrent().oAuth, User.getCurrent().id)

        return binding.root
    }
}