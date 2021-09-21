package com.nasri.messenger.ui.newmessage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasri.messenger.data.user.DummyUserService
import com.nasri.messenger.data.user.UserRepositoryImpl
import com.nasri.messenger.data.user.UserService
import com.nasri.messenger.databinding.FragmentNewMessageBinding
import com.nasri.messenger.domain.result.Result
import com.nasri.messenger.domain.user.SearchUsersUseCase
import com.nasri.messenger.domain.user.User
import com.nasri.messenger.ui.base.BaseFragment
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter


class NewMessageFragment : BaseFragment() {

    private lateinit var binding: FragmentNewMessageBinding

    private lateinit var sectionsAdapter: SectionedRecyclerViewAdapter

    private lateinit var suggestedSection: SuggestedSection

    private lateinit var morePeopleSection: MorePeopleSection

    val viewModel: NewMessageViewModel by viewModels {
        val userService: UserService = DummyUserService()
        val currentUserId = preferenceStorage.getCurrentUser()?.uid
                ?: throw IllegalStateException("Cannot get CurrentUser id")

        val userRepository = UserRepositoryImpl(userService)

        val searchUsersUseCase = SearchUsersUseCase(userRepository)

        NewMessageViewModelFactory(currentUserId, searchUsersUseCase)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // First load don't need query, will load users ordered alphabetically or according to some score
        // based on number of messages and interaction between the two users
        viewModel.onSearchQuery("")

        sectionsAdapter = SectionedRecyclerViewAdapter()
        suggestedSection = SuggestedSection()
        morePeopleSection = MorePeopleSection()

        sectionsAdapter.addSection(suggestedSection)
        sectionsAdapter.addSection(morePeopleSection)

        viewModel.userSearchResponse.observe(viewLifecycleOwner, {
            hideLoadingForAllSections()
            when (it) {
                is Result.Success -> {
                    val data = it.data
                    suggestedSection.setData(data.contacts.map(this::toPeopleItem))
                    morePeopleSection.setData(data.people.map(this::toPeopleItem))
                    sectionsAdapter.notifyDataSetChanged()
                }
                is Result.Loading -> {
                    showLoadingForAllSections()
                }
                is Result.Error -> {
                    // TODO('Handle Errors')
                }
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = sectionsAdapter

        binding.searchEditText.doAfterTextChanged {
            viewModel.onSearchQuery(it?.toString() ?: "")
        }
    }

    private fun showLoadingForAllSections() {
        onSectionLoadingStarted(suggestedSection)
        onSectionLoadingStarted(morePeopleSection)
    }

    private fun hideLoadingForAllSections() {
        onSectionLoaded(suggestedSection)
        onSectionLoaded(morePeopleSection)
    }

    private fun onSectionLoaded(section: Section) {
        val sectionAdapter = sectionsAdapter.getAdapterForSection(section)
        val state = section.state

        section.state = Section.State.LOADED
        if (state != Section.State.LOADED) {
            sectionAdapter.notifyStateChangedToLoaded(state)
        }
    }

    private fun onSectionLoadingStarted(section: Section) {
        val sectionAdapter = sectionsAdapter.getAdapterForSection(section)
        val state = section.state
        val itemsCount = section.contentItemsTotal

        section.state = Section.State.LOADING
        if (state == Section.State.LOADED) {
            sectionAdapter.notifyStateChangedFromLoaded(itemsCount)
        } else {
            if (state != Section.State.LOADING) {
                sectionAdapter.notifyNotLoadedStateChanged(state)
            }
        }
    }

    private fun toPeopleItem(user: User): PeopleItem {
        return PeopleItem(user.username!!, Uri.parse(user.photoUrl))
    }

}