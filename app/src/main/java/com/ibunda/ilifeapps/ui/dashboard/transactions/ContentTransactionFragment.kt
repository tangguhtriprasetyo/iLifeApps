package com.ibunda.ilifeapps.ui.dashboard.transactions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentContentTransactionBinding
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.utils.AppConstants.STATUS_DIBATALKAN
import com.ibunda.ilifeapps.utils.AppConstants.STATUS_DIPROSES
import com.ibunda.ilifeapps.utils.AppConstants.STATUS_PESANAN
import com.ibunda.ilifeapps.utils.AppConstants.STATUS_SELESAI


class ContentTransactionFragment : Fragment() {

    private lateinit var binding: FragmentContentTransactionBinding
    private lateinit var userDataProfile: Users

    private var userId: String? = null

    private val listTransactionAdapter = ListTransactionAdapter()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(index: Int) =
            ContentTransactionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
            1 -> {
                getUserId(STATUS_PESANAN)
            }

            2 -> {
                getUserId(STATUS_DIPROSES)
            }

            3 -> {
                getUserId(STATUS_SELESAI)
            }

            4 -> {
                getUserId(STATUS_DIBATALKAN)
            }
        }
    }

    private fun setDataTransaction(statusOrder: String) {

        Log.d("ORDERS", "userId $userId")
        transactionViewModel.getListOrders(statusOrder, userId.toString())
            .observe(viewLifecycleOwner, { listOrders ->
                when (statusOrder) {
                    STATUS_PESANAN -> {
                        if (listOrders != null && listOrders.isNotEmpty()) {
                            showEmptyPesanan(false)
                            listTransactionAdapter.setListOrders(listOrders)
                            setTransactionAdapter()
                        } else {
                            showEmptyPesanan(true)
                        }
                    }
                    STATUS_DIPROSES -> {
                        if (listOrders != null && listOrders.isNotEmpty()) {
                            showEmptyDiproses(false)
                            listTransactionAdapter.setListOrders(listOrders)
                            setTransactionAdapter()
                        } else {
                            showEmptyDiproses(true)
                        }
                    }
                    STATUS_SELESAI -> {
                        if (listOrders != null && listOrders.isNotEmpty()) {
                            showEmptySelesai(false)
                            listTransactionAdapter.setListOrders(listOrders)
                            setTransactionAdapter()
                        } else {
                            showEmptySelesai(true)
                        }
                    }
                    STATUS_DIBATALKAN -> {
                        if (listOrders != null && listOrders.isNotEmpty()) {
                            showEmptyDibatalkan(false)
                            listTransactionAdapter.setListOrders(listOrders)
                            setTransactionAdapter()
                        } else {
                            showEmptyDibatalkan(true)
                        }
                    }
                }
            })
    }

    private fun setTransactionAdapter() {
        with(binding.rvListOrderTransaction) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = listTransactionAdapter
        }
    }

    private fun getUserId(statusOrder: String) {
        userDataProfile = Users()
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userData ->
                Log.d("ORDERS", "getUserData $userData")
                if (userData != null) {
                    userDataProfile = userData
                    userId = userDataProfile.userId
                    setDataTransaction(statusOrder)
                }
            })
    }

    private fun showEmptyPesanan(state: Boolean) {
        if (state) {
            binding.rvListOrderTransaction.visibility = View.GONE
            binding.tesLayoutKosong.visibility = View.VISIBLE
            binding.tesLayoutKosong.text = "Layout Kosong $STATUS_PESANAN"
        } else {
            binding.rvListOrderTransaction.visibility = View.VISIBLE
            binding.tesLayoutKosong.visibility = View.GONE
        }
    }

    private fun showEmptyDiproses(state: Boolean) {
        if (state) {
            binding.rvListOrderTransaction.visibility = View.GONE
            binding.tesLayoutKosong.visibility = View.VISIBLE
            binding.tesLayoutKosong.text = "Layout Kosong $STATUS_DIPROSES"
        } else {
            binding.rvListOrderTransaction.visibility = View.VISIBLE
            binding.tesLayoutKosong.visibility = View.GONE
        }
    }

    private fun showEmptySelesai(state: Boolean) {
        if (state) {
            binding.rvListOrderTransaction.visibility = View.GONE
            binding.tesLayoutKosong.visibility = View.VISIBLE
            binding.tesLayoutKosong.text = "Layout Kosong $STATUS_SELESAI"
        } else {
            binding.rvListOrderTransaction.visibility = View.VISIBLE
            binding.tesLayoutKosong.visibility = View.GONE
        }
    }

    private fun showEmptyDibatalkan(state: Boolean) {
        if (state) {
            binding.rvListOrderTransaction.visibility = View.GONE
            binding.tesLayoutKosong.visibility = View.VISIBLE
            binding.tesLayoutKosong.text = "Layout Kosong $STATUS_DIBATALKAN"
        } else {
            binding.rvListOrderTransaction.visibility = View.VISIBLE
            binding.tesLayoutKosong.visibility = View.GONE
        }
    }

}