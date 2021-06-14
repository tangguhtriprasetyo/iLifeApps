package com.ibunda.ilifeapps.ui.chat.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentChatRoomBinding

class ChatRoomFragment : Fragment() {
    private lateinit var binding : FragmentChatRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }
}