package com.ibunda.ilifeapps.ui.dashboard.home.chats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.ActivityChatsBinding
import com.ibunda.ilifeapps.ui.dashboard.home.chats.listchatroom.ListChatRoomFragment

class ChatsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listChatRoomFragment = ListChatRoomFragment()
        supportFragmentManager.commit {
            replace(R.id.host_chat_activity, listChatRoomFragment)
        }
    }
}