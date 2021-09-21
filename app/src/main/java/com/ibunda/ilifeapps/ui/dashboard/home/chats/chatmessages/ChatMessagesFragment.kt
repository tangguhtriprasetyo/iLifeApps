package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentListChatMessagesBinding
import com.ibunda.ilifeapps.ui.dashboard.home.chats.ChatsViewModel
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.DateHelper
import com.ibunda.ilifeapps.utils.loadImage
import java.util.*

class ChatMessagesFragment : Fragment(), ChatMessagesClickCallback {

    private lateinit var binding: FragmentListChatMessagesBinding
    private lateinit var userDataProfile: Users

    private val chatsViewModel: ChatsViewModel by activityViewModels()
    private val chatMessagesAdapter = ChatMessagesAdapter(this@ChatMessagesFragment)

    private var chatRoom: ChatRoom = ChatRoom()

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
        initData()
        initView()
    }

    private fun initData() {

        chatsViewModel.chatRoom
            .observe(viewLifecycleOwner, { chatId ->
                if (chatId != null) {
                    chatRoom = chatId
                    setDataItem(chatRoom)
                    setDataChatMessages(chatRoom.chatRoomId!!)
                }
            })

        chatsViewModel.userData
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                }
            })

    }

    private fun setDataItem(chatRoom: ChatRoom) {
        with(binding) {
            imgProfileMitra.loadImage(chatRoom.shopPicture)
            tvNamaMitra.text = chatRoom.shopName
            tvKategoriMitra.text = chatRoom.categoryName
            tvHargaMitra.text = chatRoom.shopPrice

            if (chatRoom.verified) {
                icVerified.visibility = View.VISIBLE
            }
        }
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
        binding.btnSendMessage.setOnClickListener {
            if (binding.etChatbox.text.isNotEmpty()) {
                sendChat()
            }
        }
    }

    private fun sendChat() {
        val message: String = binding.etChatbox.text.toString()
        val chatRoom = ChatRoom(
            chatRoomId = chatRoom.chatRoomId,
            accTawar = false,
            lastDate = DateHelper.getCurrentDate(),
            lastMessage = message,
            lastHargaTawar = chatRoom.lastHargaTawar,
            lastTawar = false,
            read = false,
            shopId = chatRoom.shopId,
            shopName = chatRoom.shopName,
            shopPicture = chatRoom.shopPicture,
            categoryName = chatRoom.categoryName,
            shopPrice = chatRoom.shopPrice,
            userId = chatRoom.userId,
            userName = chatRoom.userName,
            userPicture = chatRoom.userPicture,
            verified = chatRoom.verified
        )

        val chatMessages = ChatMessages(
            date = DateHelper.getCurrentDate(),
            message = message,
            sender = chatRoom.userId,
            statusTawaran = AppConstants.STATUS_MENAWAR,
            tawar = false,
            time = DateHelper.getCurrentTime(),
            timeStamp = Timestamp(Date())
        )

        chatsViewModel.updateChatRoom(chatRoom)
            .observe(viewLifecycleOwner, { statusTawar ->
                if (statusTawar == AppConstants.STATUS_SUCCESS) {
                    chatsViewModel.sendChat(chatRoom.chatRoomId.toString(), chatMessages)
                        .observe(viewLifecycleOwner, { statusChat ->
                            if (statusChat == AppConstants.STATUS_SUCCESS) {
                                binding.etChatbox.text = null
                                Toast.makeText(
                                    requireContext(),
                                    "Chat terkirim",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(requireContext(), statusChat, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                } else {
                    Toast.makeText(requireContext(), statusTawar, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onItemClicked(data: ChatMessages) {
        // TODO onItemClicked
    }
}