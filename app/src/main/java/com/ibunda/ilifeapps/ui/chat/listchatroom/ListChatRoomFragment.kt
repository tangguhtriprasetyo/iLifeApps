package com.ibunda.ilifeapps.ui.chat.listchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentListChatRoomBinding

class ListChatRoomFragment : Fragment() {
    private lateinit var binding : FragmentListChatRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }
}