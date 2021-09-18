package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.FragmentPilihMitraBinding
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.DateHelper

class PilihMitraFragment : Fragment(), PilihMitraClickCallback {
    private lateinit var binding: FragmentPilihMitraBinding
    private lateinit var orders: Orders

    private val pilihMitraViewModel: PilihMitraViewModel by activityViewModels()
    private val pilihMitraAdapter = PilihMitraAdapter(this@PilihMitraFragment)

    companion object {
        const val EXTRA_ORDER_ID = "extra_order_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPilihMitraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListTawaranMitra()
        initView()
    }

    private fun getListTawaranMitra() {
        if (arguments != null) {
            orders = requireArguments().getParcelable<Orders>(EXTRA_ORDER_ID) as Orders
            orders?.let {
                pilihMitraViewModel.getListTawaranMitra(it.orderId.toString())
                    .observe(viewLifecycleOwner, { listMitra ->
                        if (listMitra != null && listMitra.isNotEmpty()) {
                            pilihMitraAdapter.setListMitra(listMitra)
                            setListMitraAdapter()
                            showEmptyListShop(false)
                        } else {
                            showEmptyListShop(true)
                        }
                    })
            }
        } else {
            showEmptyListShop(true)
        }
    }

    private fun setListMitraAdapter() {
        with(binding.rvPilihMitra) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = pilihMitraAdapter
            Log.d(ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }


    private fun showEmptyListShop(state: Boolean) {
        if (state) {
            binding.rvPilihMitra.visibility = View.GONE
            binding.linearEmptyPilihMitra.visibility = View.VISIBLE
        } else {
            binding.rvPilihMitra.visibility = View.VISIBLE
            binding.linearEmptyPilihMitra.visibility = View.GONE
        }
    }

    private fun updateOrderData(data: Shops) {
        orders.shopId = data.shopId
        orders.shopName = data.shopName
        orders.shopPicture = data.shopPicture
        orders.processedAt = DateHelper.getCurrentDateTime()
        orders.verified = data.verified
        orders.totalPrice = data.priceTawar
        orders.status = AppConstants.STATUS_DIPROSES
        pilihMitraViewModel.updateOrderData(orders).observe(viewLifecycleOwner, { orderData ->
            if (orderData != null) {
                sendNotif()
            }
        })
    }

    private fun sendNotif() {
        val notif = Notifications(
            body = AppConstants.MESSAGE_STATUS_PESANAN,
            date = DateHelper.getCurrentDateTime(),
            orderId = orders.orderId,
            read = false,
            receiverId = orders.shopId,
            receiverPicture = orders.shopPicture,
            title = AppConstants.TITLE_STATUS_PESANAN,
            senderId = orders.userId,
            senderPicture = orders.userPicture
        )
        pilihMitraViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
            if (status == AppConstants.STATUS_SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    "Pesanan berhasil diproses, silahkan lihat status pesanan anda di halaman Transaksi",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().onBackPressed()
            } else {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClicked(data: Shops) {
        updateOrderData(data)
    }
}