package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentListChatMessagesBinding

class ChatMessagesFragment : Fragment() {
    private lateinit var binding: FragmentListChatMessagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListChatMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }
}