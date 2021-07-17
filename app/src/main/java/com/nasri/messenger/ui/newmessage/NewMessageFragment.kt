package com.nasri.messenger.ui.newmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nasri.messenger.databinding.FragmentNewMessageBinding
import com.nasri.messenger.ui.base.BaseFragment

class NewMessageFragment : BaseFragment() {

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
    }

}