package com.ibunda.ilifeapps.ui.dashboard.home.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.ActivityChatBinding
import com.ibunda.ilifeapps.ui.dashboard.home.chat.listchatroom.ListChatRoomFragment

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listChatRoomFragment = ListChatRoomFragment()
        supportFragmentManager.commit {
            replace(R.id.host_chat_activity, listChatRoomFragment)
        }
    }
}