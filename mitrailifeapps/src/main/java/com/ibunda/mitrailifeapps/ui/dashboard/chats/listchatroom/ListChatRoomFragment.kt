package com.ibunda.mitrailifeapps.ui.dashboard.chats.listchatroom

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.ChatRoom
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentListChatRoomBinding
import com.ibunda.mitrailifeapps.ui.dashboard.chats.ChatsViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.chats.chatmessages.ChatMessagesFragment
import com.ibunda.mitrailifeapps.utils.AppConstants
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper

class ListChatRoomFragment : Fragment(), ListChatRoomClickCallback {

    private lateinit var binding : FragmentListChatRoomBinding
    private lateinit var shopsDataProfile: Shops

    private val chatsViewModel: ChatsViewModel by activityViewModels()
    private val listChatRoomAdapter = ListChatRoomAdapter(this@ListChatRoomFragment)

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialogHelper.progressDialog(requireContext())
        initView()
        initData()
    }

    private fun initData() {
        chatsViewModel.shopData
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    setDataChatRoom()
                }
            })
    }

    private fun setDataChatRoom() {
        showProgressBar(true)
        chatsViewModel.getListChatRoom(shopsDataProfile.shopId.toString())
            .observe(viewLifecycleOwner, { chatRoom ->
                if (chatRoom != null && chatRoom.isNotEmpty()) {
                    showProgressBar(false)
                    listChatRoomAdapter.setListChatRoom(chatRoom)
                    countNotRead(chatRoom)
                    setChatRoomAdapter()
                    showEmptChatRoom(false)
                } else {
                    showProgressBar(false)
                    showEmptChatRoom(true)
                    binding.tvBelumDibaca.text = "(" + 0.toString() + ")"
                }
            })
    }

    private fun countNotRead(chatRoom: List<ChatRoom>) {
        var count = 0
        for (item in chatRoom) {
            if (!item.readByShop) {
                count += 1
            }
        }
        val total = "( $count )"
        Log.d(total, "totalBelumDibaca")
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

    private fun showProgressBar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setChatRoomAdapter() {
        with(binding.rvRoomChat) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listChatRoomAdapter
        }
    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onItemClicked(data: ChatRoom) {
        progressDialog.show()
        chatsViewModel.setChatRoomId(data.chatRoomId!!)
        chatsViewModel.readChat(data.chatRoomId!!)
            .observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    progressDialog.dismiss()
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
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
    }
}