package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentListChatMessagesBinding
import com.ibunda.ilifeapps.ui.dashboard.home.chats.ChatsViewModel

class ChatMessagesFragment : Fragment(), ChatMessagesClickCallback {

    private lateinit var binding: FragmentListChatMessagesBinding
    private lateinit var userDataProfile: Users

    private val chatsViewModel: ChatsViewModel by activityViewModels()
    private val chatMessagesAdapter = ChatMessagesAdapter(this@ChatMessagesFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListChatMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initData() {
        chatsViewModel.chatRoomId
            .observe(viewLifecycleOwner, { chatRoomId ->
                if (chatRoomId != null) {
                    setDataChatMessages(chatRoomId)
                }
            })

        chatsViewModel.userData
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                }
            })
    }

    private fun setDataChatMessages(chatRoomId: String) {
        chatsViewModel.getListChatMessages(chatRoomId)
            .observe(viewLifecycleOwner, { listChatMessages ->
                if (listChatMessages != null && listChatMessages.isNotEmpty()) {
                    chatMessagesAdapter.setListChatMessages(
                        listChatMessages,
                        userDataProfile.userId
                    )
                    setChatMessagesAdapter()
                    showEmptChatMessages(false)
                } else {
                    showEmptChatMessages(true)
                }
            })
    }

    private fun showEmptChatMessages(state: Boolean) {
        if (state) {
            binding.rvChats.visibility = View.GONE
        } else {
            binding.rvChats.visibility = View.VISIBLE
        }
    }

    private fun setChatMessagesAdapter() {
        with(binding.rvChats) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = chatMessagesAdapter
        }
    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onItemClicked(data: ChatMessages) {
        // TODO onItemClicked
    }
}