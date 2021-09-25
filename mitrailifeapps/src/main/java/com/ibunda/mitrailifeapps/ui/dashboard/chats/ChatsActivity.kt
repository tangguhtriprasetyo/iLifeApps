package com.ibunda.mitrailifeapps.ui.dashboard.chats

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.ChatRoom
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.ActivityChatsBinding
import com.ibunda.mitrailifeapps.ui.dashboard.chats.chatmessages.ChatMessagesFragment
import com.ibunda.mitrailifeapps.ui.dashboard.chats.listchatroom.ListChatRoomFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsBinding

    private var shopData: Shops = Shops()
    private var chatRoom: ChatRoom = ChatRoom()

    private val chatsViewModel: ChatsViewModel by viewModels()

    companion object {
        const val EXTRA_ROOM_ID = "extra_room_id"
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra(EXTRA_USER)) {
            shopData = intent.getParcelableExtra<Shops>(EXTRA_USER) as Shops
            chatsViewModel.setShopData(shopData)
        }
        if (intent.hasExtra(EXTRA_ROOM_ID)) {
            chatRoom = intent.getParcelableExtra<ChatRoom>(EXTRA_ROOM_ID) as ChatRoom
            chatsViewModel.setChatRoomId(chatRoom.chatRoomId!!)
            val chatMessagesFragment = ChatMessagesFragment()
            setCurrentFragment(chatMessagesFragment, ChatMessagesFragment::class.java.simpleName)
        } else {
            val listChatRoomFragment = ListChatRoomFragment()
            setCurrentFragment(listChatRoomFragment, ListChatRoomFragment::class.java.simpleName)
        }
    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.host_fragment_activity_chat, fragment, fragmentTag)
        }
    }

}