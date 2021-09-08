package com.ibunda.mitrailifeapps.ui.dashboard.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentContentTransactionBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.order.OrderAdapter
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_DIBATALKAN
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_DIPROSES
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_SELESAI
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class ContentTransactionFragment : Fragment() {

    private lateinit var binding: FragmentContentTransactionBinding

    private lateinit var shopsDataProfile: Shops
    private val mainViewModel: MainViewModel by activityViewModels()
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val orderAdapter = OrderAdapter()

    private var shopId: String? = null

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
                getShopId(STATUS_DIPROSES)
            }
            2 -> {
                getShopId(STATUS_SELESAI)
            }
            3 -> {
                getShopId(STATUS_DIBATALKAN)
            }
        }
    }

    private fun getShopId(statusOrder: String) {
        shopsDataProfile = Shops()
        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                Log.d("ORDERS", "getUserData $shopsProfile")
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    shopId = shopsProfile.shopId
                    setDataTransaction(statusOrder)
                }
            })
    }

    private fun setDataTransaction(statusOrder: String) {

        Log.d("ORDERS", "shopId $shopId")
        transactionViewModel.getListOrders(statusOrder, shopId.toString())
            .observe(viewLifecycleOwner, { listOrders ->
                when (statusOrder) {
                    STATUS_DIPROSES -> {
                        if (listOrders != null && listOrders.isNotEmpty()) {
                            showEmptyDiproses(false)
                            orderAdapter.setListOrders(listOrders)
                            setTransactionAdapter()
                        } else {
                            showEmptyDiproses(true)
                        }
                    }
                    STATUS_SELESAI -> {
                        if (listOrders != null && listOrders.isNotEmpty()) {
                            showEmptySelesai(false)
                            orderAdapter.setListOrders(listOrders)
                            setTransactionAdapter()
                        } else {
                            showEmptySelesai(true)
                        }
                    }
                    STATUS_DIBATALKAN -> {
                        if (listOrders != null && listOrders.isNotEmpty()) {
                            showEmptyDibatalkan(false)
                            orderAdapter.setListOrders(listOrders)
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
            adapter = orderAdapter
        }
    }

    private fun showEmptyDiproses(state: Boolean) {
        if (state) {
            binding.rvListOrderTransaction.visibility = View.GONE
            binding.linearEmptyDiproses.visibility = View.VISIBLE
        } else {
            binding.rvListOrderTransaction.visibility = View.VISIBLE
        }
    }

    private fun showEmptySelesai(state: Boolean) {
        if (state) {
            binding.rvListOrderTransaction.visibility = View.GONE
            binding.linearEmptySelesai.visibility = View.VISIBLE
        } else {
            binding.rvListOrderTransaction.visibility = View.VISIBLE
        }
    }

    private fun showEmptyDibatalkan(state: Boolean) {
        if (state) {
            binding.rvListOrderTransaction.visibility = View.GONE
            binding.linearEmptyDibatalkan.visibility = View.VISIBLE
        } else {
            binding.rvListOrderTransaction.visibility = View.VISIBLE
        }
    }

}