package com.ibunda.mitrailifeapps.ui.dashboard.chats.chatmessages

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.ibunda.mitrailifeapps.data.model.ChatMessages
import com.ibunda.mitrailifeapps.data.model.ChatRoom
import com.ibunda.mitrailifeapps.databinding.FragmentChatMessagesBinding
import com.ibunda.mitrailifeapps.ui.dashboard.chats.ChatsViewModel
import com.ibunda.mitrailifeapps.utils.AppConstants
import com.ibunda.mitrailifeapps.utils.DateHelper
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class ChatMessagesFragment : Fragment() {

    private lateinit var binding: FragmentChatMessagesBinding

    private val chatsViewModel: ChatsViewModel by activityViewModels()
    private val chatMessagesAdapter = ChatMessagesAdapter()
    private var chatRoom: ChatRoom = ChatRoom()
    private var listChatSize: Int = 0
    private lateinit var progressDialog: Dialog

    companion object {
        const val TOLAK_TAWARAN = "Tawaran ditolak!"
        const val TERIMA_TAWARAN = "Tawaran diterima!"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())
        initData()
        initOnClick()
    }

    private fun initData() {

        chatsViewModel.chatRoom
            .observe(viewLifecycleOwner, { chatId ->
                if (chatId != null) {
                    getChatRoomData(chatId)
                }
            })
    }

    private fun initView(chatRoom: ChatRoom) {
        with(binding) {
            imgProfileUser.loadImage(chatRoom.userPicture)
            tvNamaUser.text = chatRoom.userName
            if (chatRoom.lastTawar) {
                binding.linearBgTawarPesan.visibility = View.VISIBLE
            } else {
                binding.linearBgTawarPesan.visibility = View.GONE
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
                            chatRoom.shopId
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

    private fun getChatRoomData(chatId: String) {
        chatsViewModel.getChatRoomData(chatId).observe(viewLifecycleOwner, { chatRoomData ->
            if (chatRoomData != null) {
                chatRoom = chatRoomData
                setDataChatMessages(chatRoom.chatRoomId!!)
                initView(chatRoom)
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
        binding.btnTolakTawaran.setOnClickListener {
            kelolaTawaran(TOLAK_TAWARAN)
        }
        binding.btnTerimaTawaran.setOnClickListener {
            kelolaTawaran(TERIMA_TAWARAN)
        }
        binding.btnSendMessage.setOnClickListener {
            if (binding.etChatbox.text.isNotEmpty()) {
                sendChat()
            }
        }
    }

    private fun kelolaTawaran(result: String) {
        progressDialog.show()
        val chatRoom = ChatRoom(
            chatRoomId = chatRoom.chatRoomId,
            lastDate = DateHelper.getCurrentDate(),
            lastHargaTawar = chatRoom.lastHargaTawar,
            lastTawar = false,
            readByUser = false,
            readByShop = true,
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
            sender = chatRoom.shopId,
            statusTawaran = AppConstants.STATUS_MENAWAR,
            tawar = true,
            time = DateHelper.getCurrentTime(),
            timeStamp = Timestamp(Date())
        )
        if (result == TOLAK_TAWARAN) {
            chatRoom.lastMessage = TOLAK_TAWARAN
            chatRoom.accTawar = false
            chatMessages.message = TOLAK_TAWARAN
            chatsViewModel.updateChatRoom(chatRoom)
                .observe(viewLifecycleOwner, { statusTawar ->
                    if (statusTawar == AppConstants.STATUS_SUCCESS) {
                        chatsViewModel.sendChat(chatRoom.chatRoomId.toString(), chatMessages)
                            .observe(viewLifecycleOwner, { statusChat ->
                                if (statusChat == AppConstants.STATUS_SUCCESS) {
                                    requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
                                    initData()
                                    binding.etChatbox.text = null
                                    Toast.makeText(
                                        requireContext(),
                                        "Berhasil menolak tawaran.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    progressDialog.dismiss()
                                } else {
                                    Toast.makeText(requireContext(), statusChat, Toast.LENGTH_SHORT)
                                        .show()
                                    progressDialog.dismiss()
                                }
                            })
                    } else {
                        Toast.makeText(requireContext(), statusTawar, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                })
        } else {
            chatRoom.lastMessage = TERIMA_TAWARAN
            chatRoom.accTawar = true
            chatMessages.message = TERIMA_TAWARAN
            chatsViewModel.updateChatRoom(chatRoom)
                .observe(viewLifecycleOwner, { statusTawar ->
                    if (statusTawar == AppConstants.STATUS_SUCCESS) {
                        chatsViewModel.sendChat(chatRoom.chatRoomId.toString(), chatMessages)
                            .observe(viewLifecycleOwner, { statusChat ->
                                if (statusChat == AppConstants.STATUS_SUCCESS) {
                                    requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
                                    initData()
                                    binding.etChatbox.text = null
                                    Toast.makeText(
                                        requireContext(),
                                        "Berhasil menerima tawaran.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    progressDialog.dismiss()
                                } else {
                                    Toast.makeText(requireContext(), statusChat, Toast.LENGTH_SHORT)
                                        .show()
                                    progressDialog.dismiss()
                                }
                            })
                    } else {
                        Toast.makeText(requireContext(), statusTawar, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                })
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
            readByUser = false,
            readByShop = true,
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
            sender = chatRoom.shopId,
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



}