package com.nasri.messenger.ui.newmessage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nasri.messenger.databinding.FragmentNewMessageBinding
import com.nasri.messenger.ui.base.BaseFragment
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter


class NewMessageFragment : BaseFragment() {
    companion object {
        val DUMMY_PEOPLE_LIST_1 = listOf(
            PeopleItem("Gene Hayes", Uri.parse("https://randomuser.me/api/portraits/men/96.jpg")),
            PeopleItem(
                "Carrie Prescott",
                Uri.parse("https://randomuser.me/api/portraits/women/39.jpg")
            ),
            PeopleItem(
                "Logan Bennett",
                null
            ),
            PeopleItem(
                "Madison Romero",
                Uri.parse("https://randomuser.me/api/portraits/women/42.jpg")
            ),
            PeopleItem("Levi Brown", Uri.parse("https://randomuser.me/api/portraits/men/43.jpg")),
            PeopleItem(
                "Kelly Morrison",
                Uri.parse("https://randomuser.me/api/portraits/men/95.jpg")
            ),
            PeopleItem("Darren White", Uri.parse("https://randomuser.me/api/portraits/men/24.jpg")),
            PeopleItem(
                "Ashley Oliver",
                Uri.parse("https://randomuser.me/api/portraits/women/20.jpg")
            ),
        )

        val DUMMY_PEOPLE_LIST_2 = listOf(
            PeopleItem(
                "Loretta Caldwell",
                Uri.parse("https://randomuser.me/api/portraits/women/37.jpg")
            ),
            PeopleItem(
                "Tyler Fox",
                Uri.parse("https://randomuser.me/api/portraits/men/16.jpg")
            ),
            PeopleItem(
                "Brent Terry",
                null
            ),

            )
    }

    private lateinit var binding: FragmentNewMessageBinding

    private val viewModel: NewMessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionAdapter = SectionedRecyclerViewAdapter()
        sectionAdapter.addSection(SuggestedSection(DUMMY_PEOPLE_LIST_2))
        sectionAdapter.addSection(MorePeopleSection(DUMMY_PEOPLE_LIST_1))

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = sectionAdapter
    }

}