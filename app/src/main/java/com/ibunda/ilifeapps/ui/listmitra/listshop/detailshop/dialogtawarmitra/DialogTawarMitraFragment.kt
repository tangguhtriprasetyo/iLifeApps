package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.dialogtawarmitra

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import com.ibunda.ilifeapps.data.model.ChatMessages
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentDialogTawarMitraBinding
import com.ibunda.ilifeapps.ui.dashboard.home.chats.ChatsActivity
import com.ibunda.ilifeapps.ui.listmitra.ListMitraViewModel
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.DateHelper
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class DialogTawarMitraFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogTawarMitraBinding
    private var shopData: Shops? = null
    private var userData: Users? = null
    private var chatRooms: ChatRoom? = null

    private val listMitraViewModel: ListMitraViewModel by activityViewModels()

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_SHOP = "extra_shop"
        const val EXTRA_CHATROOM = "extra_chatroom"
        const val FROM_CHAT = "from_chat"
        const val TAWAR = "tawar"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogTawarMitraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            shopData = requireArguments().getParcelable(EXTRA_SHOP)
            userData = requireArguments().getParcelable(EXTRA_USER)

            if (requireArguments().getBoolean(FROM_CHAT)) {
                chatRooms = requireArguments().getParcelable(EXTRA_CHATROOM)
                initView()
                binding.btnBuatTawaran.setOnClickListener {
                    sendTawaran(FROM_CHAT)
                }
            } else {
                binding.btnBuatTawaran.setOnClickListener {
                    sendTawaran(TAWAR)
                }
            }
        }
        initView()


    }

    private fun initView() {

        binding.icClose.setOnClickListener {
            onDismiss(dialog!!)
        }
        binding.etTawar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                binding.btnBuatTawaran.isEnabled = true
            }
        })
    }

    private fun sendTawaran(result: String) {
        val tawarPrice: Int = binding.etTawar.text.toString().toInt()
        val tawarPriceFormat = PriceFormatHelper.getPriceFormat(tawarPrice)
        val chatRoom = ChatRoom(
            accTawar = false,
            lastDate = DateHelper.getCurrentDate(),
            lastMessage = "Melakukan Penawaran $tawarPriceFormat",
            lastHargaTawar = tawarPrice,
            lastTawar = true,
            readByUser = true,
            readByShop = false,
            userId = userData?.userId,
            userName = userData?.name,
            userPicture = userData?.avatar,
            verified = shopData?.verified ?: false
        )

        val chatMessages = ChatMessages(
            date = DateHelper.getCurrentDate(),
            message = "Melakukan Penawaran $tawarPriceFormat",
            sender = userData?.userId,
            statusTawaran = AppConstants.STATUS_MENAWAR,
            tawar = true,
            time = DateHelper.getCurrentTime(),
            timeStamp = Timestamp(Date())
        )

        if (result == TAWAR) {
            chatRoom.chatRoomId = userData?.userId + shopData?.shopId
            chatRoom.shopId = shopData?.shopId
            chatRoom.shopName = shopData?.shopName
            chatRoom.shopPicture = shopData?.shopPicture
            chatRoom.categoryName = shopData?.categoryName
            chatRoom.shopPrice = PriceFormatHelper.getPriceFormat(shopData?.price)
            listMitraViewModel.uploadTawaran(chatRoom)
                .observe(viewLifecycleOwner, { statusTawar ->
                    if (statusTawar == AppConstants.STATUS_SUCCESS) {
                        listMitraViewModel.sendChat(chatRoom.chatRoomId.toString(), chatMessages)
                            .observe(viewLifecycleOwner, { statusChat ->
                                if (statusChat == AppConstants.STATUS_SUCCESS) {
                                    val intent =
                                        Intent(requireActivity(), ChatsActivity::class.java)
                                    intent.putExtra(ChatsActivity.EXTRA_ROOM_ID, chatRoom)
                                    intent.putExtra(ChatsActivity.EXTRA_USER, userData)
                                    startActivity(intent)
                                    requireActivity().finish()
                                    onDismiss(dialog!!)
                                } else {
                                    Toast.makeText(requireContext(), statusChat, Toast.LENGTH_SHORT)
                                        .show()
                                    onDismiss(dialog!!)
                                }
                            })
                    } else {
                        Toast.makeText(requireContext(), statusTawar, Toast.LENGTH_SHORT).show()
                        onDismiss(dialog!!)
                    }
                })
        } else {
            Log.e("frommmTAWAR", "fromTawaaarrrrrrrr")
            val chatRoomId= chatRooms?.chatRoomId
            chatRoom.chatRoomId = chatRoomId
            chatRoom.shopId = chatRooms?.shopId
            chatRoom.shopName = chatRooms?.shopName
            chatRoom.shopPicture = chatRooms?.shopPicture
            chatRoom.categoryName = chatRooms?.categoryName
            chatRoom.shopPrice = chatRooms?.shopPrice
            listMitraViewModel.uploadTawaran(chatRoom)
                .observe(viewLifecycleOwner, { statusTawar ->
                    if (statusTawar == AppConstants.STATUS_SUCCESS) {
                        listMitraViewModel.sendChat(chatRoomId.toString(), chatMessages)
                            .observe(viewLifecycleOwner, { statusChat ->
                                if (statusChat == AppConstants.STATUS_SUCCESS) {
                                    onDismiss(dialog!!)
                                } else {
                                    Toast.makeText(requireContext(), statusChat, Toast.LENGTH_SHORT)
                                        .show()
                                    onDismiss(dialog!!)
                                }
                            })
                    } else {
                        Toast.makeText(requireContext(), statusTawar, Toast.LENGTH_SHORT).show()
                        onDismiss(dialog!!)
                    }
                })
        }

    }


}