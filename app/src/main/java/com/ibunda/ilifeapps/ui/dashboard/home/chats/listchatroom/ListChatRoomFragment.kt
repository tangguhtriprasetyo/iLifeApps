package com.ibunda.ilifeapps.ui.dashboard.home.chats.listchatroom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentListChatRoomBinding
import com.ibunda.ilifeapps.ui.dashboard.home.chats.ChatsViewModel
import com.ibunda.ilifeapps.ui.dashboard.home.chats.chatmessages.ChatMessagesFragment
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.ProgressDialogHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ListChatRoomFragment : Fragment(), ListChatRoomClickCallback {

    private lateinit var binding: FragmentListChatRoomBinding
    private lateinit var userDataProfile: Users

    private val chatsViewModel: ChatsViewModel by activityViewModels()
    private val listChatRoomAdapter = ListChatRoomAdapter(this@ListChatRoomFragment)

    private lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())

        progressDialog.show()
        initView()
        initData()
    }

    private fun initData() {
        chatsViewModel.userData
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    setDataChatRoom()
                }
            })
    }

    private fun setDataChatRoom() {
        chatsViewModel.getListChatRoom(userDataProfile.userId.toString())
            .observe(viewLifecycleOwner, { chatRoom ->
                if (chatRoom != null && chatRoom.isNotEmpty()) {
                    listChatRoomAdapter.setListChatRoom(chatRoom)
                    countNotRead(chatRoom)
                    setChatRoomAdapter()
                    showEmptChatRoom(false)
                } else {
                    showEmptChatRoom(true)
                    progressDialog.dismiss()
                }
            })
    }

    private fun countNotRead(chatRoom: List<ChatRoom>) {
        var count = 0
        for (item in chatRoom) {
            if (!item.readByUser) {
                count += 1
            }
        }
        val total = "( $count )"
        binding.tvBelumDibaca.text = total
    }

    private fun showEmptChatRoom(state: Boolean) {
        if (state) {
            binding.rvRoomChat.visibility = View.GONE
            binding.linearEmptyChat.visibility = View.VISIBLE
        } else {
            binding.linearEmptyChat.visibility = View.GONE
            binding.rvRoomChat.visibility = View.VISIBLE
        }
    }

    private fun setChatRoomAdapter() {
        with(binding.rvRoomChat) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listChatRoomAdapter
        }
        progressDialog.dismiss()
    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onItemClicked(data: ChatRoom) {
        chatsViewModel.setChatRoomId(data.chatRoomId!!)
        chatsViewModel.readChat(data.chatRoomId!!)
            .observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {

                    val mFragmentManager = parentFragmentManager
                    val mChatMessagesFragment = ChatMessagesFragment()
                    mFragmentManager.commit {
                        addToBackStack(null)
                        replace(
                            R.id.host_fragment_activity_chat,
                            mChatMessagesFragment,
                            ChatMessagesFragment::class.java.simpleName
                        )
                    }

                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
    }
}