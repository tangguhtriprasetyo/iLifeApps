package com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.dialogtawarmitra.DialogTawarMitraFragment
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.DateHelper
import com.ibunda.ilifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class ChatMessagesFragment : Fragment(), ChatMessagesClickCallback {

    private lateinit var binding: FragmentListChatMessagesBinding
    private lateinit var userDataProfile: Users

    private val chatsViewModel: ChatsViewModel by activityViewModels()
    private val chatMessagesAdapter = ChatMessagesAdapter(this@ChatMessagesFragment)

    private var chatRoom: ChatRoom = ChatRoom()
    private var listChatSize: Int = 0

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
        initOnClick()
    }

    private fun initData() {

        chatsViewModel.userData
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    Log.e(userDataProfile.userId, "userrrIddddd")
                }
            })

        chatsViewModel.chatRoom
            .observe(viewLifecycleOwner, { chatId ->
                if (chatId != null) {
                    getChatRoomData(chatId)
                }
            })

    }

    private fun getChatRoomData(chatId: String) {
        chatsViewModel.getChatRoomData(chatId).observe(viewLifecycleOwner, { chatRoomData ->
            if (chatRoomData != null) {
                chatRoom = chatRoomData
                setDataChatMessages(chatRoom.chatRoomId!!)
                initView(chatRoom)
            }
        })
    }

    private fun initView(chatRoom: ChatRoom) {
        with(binding) {
            imgProfileMitra.loadImage(chatRoom.shopPicture)
            tvNamaMitra.text = chatRoom.shopName
            tvKategoriMitra.text = chatRoom.categoryName
            tvHargaMitra.text = chatRoom.shopPrice

            if (chatRoom.verified) {
                icVerified.visibility = View.VISIBLE
            } else {
                icVerified.visibility = View.GONE
            }

            if (!chatRoom.lastTawar) {
                linearBgTawarPesan.visibility = View.VISIBLE

                if (chatRoom.accTawar) {
                    btnTawarMitra.visibility = View.GONE
                } else {
                    btnTawarMitra.visibility = View.VISIBLE
                }

            } else {
                linearBgTawarPesan.visibility = View.GONE
            }
        }
    }

    private fun setDataChatMessages(chatRoomId: String) {
        chatsViewModel.getListChatMessages(chatRoomId)
            .observe(viewLifecycleOwner, { listChatMessages ->
                if (listChatMessages != null && listChatMessages.isNotEmpty()) {
                    val listSize = listChatMessages.size
                    if (listSize != listChatSize) {
                        getChatRoomData(chatRoomId)
                        chatMessagesAdapter.setListChatMessages(
                            listChatMessages,
                            userDataProfile.userId
                        )
                        setChatMessagesAdapter()
                        showEmptChatMessages(false)
                        listChatSize = listSize
                    }
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
            smoothScrollToPosition(bottom)
            adapter = chatMessagesAdapter
        }
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnSendMessage.setOnClickListener {
            if (binding.etChatbox.text.isNotEmpty()) {
                sendChat()
            }
        }
        binding.btnPesan.setOnClickListener {
            sendOrder()
        }
        binding.linearProfileChat.setOnClickListener {
            goToProfile()
        }

        binding.btnTawarMitra.setOnClickListener {
            val mDialogTawarMitraFragment = DialogTawarMitraFragment()
            val mBundle = Bundle()
            mBundle.putParcelable(DialogTawarMitraFragment.EXTRA_CHATROOM, chatRoom)
            mBundle.putParcelable(DialogTawarMitraFragment.EXTRA_USER, userDataProfile)
            mBundle.putBoolean(DialogTawarMitraFragment.FROM_CHAT, true)
            mDialogTawarMitraFragment.arguments = mBundle
            mDialogTawarMitraFragment.show(
                requireActivity().supportFragmentManager,
                DialogTawarMitraFragment::class.java.simpleName
            )
        }
    }

    private fun sendOrder() {
        val intent =
            Intent(requireActivity(), ListMitraActivity::class.java)
        if (chatRoom.accTawar) {
            intent.putExtra(ListMitraActivity.EXTRA_LAST_TAWAR, chatRoom.lastHargaTawar)
        }
        intent.putExtra(ListMitraActivity.EXTRA_SHOP, chatRoom.shopId)
        intent.putExtra(ListMitraActivity.EXTRA_ORDER_AGAIN, true)
        intent.putExtra(ListMitraActivity.EXTRA_USER, chatRoom.userId)
        startActivity(intent)
    }

    private fun goToProfile() {
        val intent =
            Intent(requireActivity(), ListMitraActivity::class.java)
        intent.putExtra(ListMitraActivity.EXTRA_SHOP, chatRoom.shopId)
        intent.putExtra(ListMitraActivity.EXTRA_TRANSACTION, true)
        intent.putExtra(ListMitraActivity.EXTRA_USER, chatRoom.userId)
        startActivity(intent)
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
            readByUser = true,
            readByShop = false,
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