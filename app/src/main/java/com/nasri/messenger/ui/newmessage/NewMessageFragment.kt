package com.nasri.messenger.ui.newmessage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.nasri.messenger.data.user.*
import com.nasri.messenger.databinding.FragmentNewMessageBinding
import com.nasri.messenger.domain.result.data
import com.nasri.messenger.domain.result.succeeded
import com.nasri.messenger.domain.user.UserSearchUseCase
import com.nasri.messenger.ui.base.BaseFragment
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter


class NewMessageFragment : BaseFragment() {

    private lateinit var binding: FragmentNewMessageBinding

    private lateinit var sectionsAdapter: SectionedRecyclerViewAdapter

    val viewModel: NewMessageViewModel by viewModels {
        val firebaseService = FirebaseUserService(FirebaseFirestore.getInstance())
        val dummyService = DummyUserService()
        val userId = preferenceStorage.getCurrentUserInfo()?.getUid() ?: ""

        val contactRepository = ContactRepository(userId, dummyService)
        val peopleRepository = PeopleRepository(dummyService)

        val searchUsersUseCase = UserSearchUseCase(contactRepository, peopleRepository)

        NewMessageViewModelFactory(userId, searchUsersUseCase)
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
        val suggestedSection = SuggestedSection()
        val morePeopleSection = MorePeopleSection()

        sectionsAdapter.addSection(suggestedSection)
        sectionsAdapter.addSection(morePeopleSection)

        viewModel.userSearchResponse.observe(viewLifecycleOwner, {
            if (it.succeeded) {
                suggestedSection.setData(it.data!!.contacts.map { contact -> toPeopleItem(contact) })
                morePeopleSection.setData(it.data!!.people.map { people -> toPeopleItem(people) })
                sectionsAdapter.notifyDataSetChanged()
            } else {
                // TODO('Handle failing')
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = sectionsAdapter

        binding.searchEditText.doAfterTextChanged {
            viewModel.onSearchQuery(it?.toString() ?: "")
        }

        viewModel.showProgress.observe(viewLifecycleOwner, { isShowProgress ->
            if (isShowProgress == true) {
                onSectionLoadingStarted(suggestedSection)
                onSectionLoadingStarted(morePeopleSection)
            } else {
                onSectionLoaded(suggestedSection)
                onSectionLoaded(morePeopleSection)
            }
        })
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
            if (state != Section.State.LOADING)
                sectionAdapter.notifyNotLoadedStateChanged(state)
        }
    }

    private fun toPeopleItem(userData: UserData): PeopleItem {
        return PeopleItem(userData.name, Uri.parse(userData.avatarUrl))
    }

}