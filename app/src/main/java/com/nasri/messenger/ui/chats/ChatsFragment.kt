package com.nasri.messenger.ui.chats

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.R
import com.nasri.messenger.databinding.FragmentChatsBinding
import com.nasri.messenger.domain.chat.LoadRecentChatsUseCase
import com.nasri.messenger.ui.base.BaseFragment
import timber.log.Timber


class ChatsFragment : BaseFragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var chatsAdapter: ChatsAdapter
    private lateinit var binding: FragmentChatsBinding

    private val viewModel: ChatsViewModel by viewModels {
        ChatsViewModelFactory(
            LoadRecentChatsUseCase(
                FirebaseFirestore.getInstance()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()
        Timber.d("User UID : %s", firebaseAuth.currentUser?.uid ?: "*")
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        chatsAdapter = ChatsAdapter(emptyList())
        binding.chatsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatsFragment.requireContext())
            addItemDecoration(
                ChatsItemDecoration(requireContext())
            )
            adapter = chatsAdapter
        }


        viewModel.recentChats.observe(viewLifecycleOwner, {
            if (it != null) {
                Timber.d("Chats Loaded")
                chatsAdapter.setChats(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }


}