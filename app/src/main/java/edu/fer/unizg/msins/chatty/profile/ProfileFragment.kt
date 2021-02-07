package edu.fer.unizg.msins.chatty.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import edu.fer.unizg.msins.chatty.R
import edu.fer.unizg.msins.chatty.login.User
import edu.fer.unizg.msins.chatty.chats.ZoomOutPageTransformer
import edu.fer.unizg.msins.chatty.chats.attachViewPager2
import edu.fer.unizg.msins.chatty.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.eventTwitchUserInfoFetched.observe(viewLifecycleOwner, Observer { avatarUrl ->
            Glide.with(this).load(avatarUrl).into(profile_image)
        })
        viewModel.getUserByOauth(User.getCurrent().oAuth)

        binding.profilePager.adapter = createViewPager2Adapter(viewModel)
        binding.let {
            it.profileTabs.attachViewPager2(
                it.profilePager,
                listOf("Following", "Followers", "Friends")
            )
        }
        binding.profilePager.setPageTransformer(ZoomOutPageTransformer())
        binding.profileTabs.tabMode = TabLayout.MODE_FIXED
        binding.profileTabs.tabGravity = TabLayout.GRAVITY_FILL
        showBackButton()
        return binding.root
    }

    private fun showBackButton() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarProfile as Toolbar)
        (binding.toolbarProfile as Toolbar).apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToMultipleChatFragment()
                findNavController().navigate(action)
            }
            title = getString(R.string.profile)
        }
    }

    private fun createViewPager2Adapter(viewModel: ProfileViewModel): RecyclerView.Adapter<*> {
        return object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> FollowingFragment()
                    1 -> FollowersFragment()
                    2 -> FriendsFragment()
                    else -> throw NotImplementedError()
                }
            }

            override fun getItemCount(): Int {
                return 3
            }

        }
    }

}