package com.ibunda.ilifeapps.ui.dashboard.home.chats

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ActivityChatsBinding
import com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages.ChatMessagesFragment
import com.ibunda.ilifeapps.ui.dashboard.home.chats.listchatroom.ListChatRoomFragment

class ChatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatsBinding

    private var userData: Users = Users()

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
            userData = intent.getParcelableExtra<Users>(EXTRA_USER) as Users
            chatsViewModel.setUserData(userData)
        }
        if (intent.hasExtra(EXTRA_ROOM_ID)) {
            var chatRoomId = intent.getStringExtra(EXTRA_ROOM_ID)
            chatsViewModel.setChatRoomId(chatRoomId!!)
            val chatMessagesFragment = ChatMessagesFragment()
            setCurrentFragment(chatMessagesFragment, ChatMessagesFragment::class.java.simpleName)
        } else {
            val listChatRoomFragment = ListChatRoomFragment()
            setCurrentFragment(listChatRoomFragment, ListChatRoomFragment::class.java.simpleName)
        }
    }

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.host_chat_activity, fragment, fragmentTag)
        }
    }
}