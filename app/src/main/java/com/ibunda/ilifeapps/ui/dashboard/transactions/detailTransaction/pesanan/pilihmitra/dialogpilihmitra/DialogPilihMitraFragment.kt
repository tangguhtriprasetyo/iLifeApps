package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra.dialogpilihmitra

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.FragmentDialogPilihMitraBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra.PilihMitraViewModel
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.DateHelper
import com.ibunda.ilifeapps.utils.ProgressDialogHelper

class DialogPilihMitraFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogPilihMitraBinding

    private var shopData: Shops? = null
    private var orders: Orders? = null
    private val pilihMitraViewModel: PilihMitraViewModel by activityViewModels()

    private lateinit var progressDialog: Dialog

    companion object {
        const val EXTRA_SHOP = "extra_shop"
        const val EXTRA_ORDER = "extra_order"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogPilihMitraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())
        shopData = requireArguments().getParcelable(EXTRA_SHOP)
        orders = requireArguments().getParcelable(EXTRA_ORDER)
        Log.d(orders?.orderId, "orderIdPilihMitra")
        binding.tvShopName.text = shopData?.shopName

        binding.btnTidak.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnPilih.setOnClickListener {
            updateOrderData(shopData, orders!!)
        }
    }

    private fun updateOrderData(shopData: Shops?, orders: Orders) {
        progressDialog.show()
        orders.shopId = shopData?.shopId
        orders.shopName = shopData?.shopName
        orders.shopPicture = shopData?.shopPicture
        orders.processedAt = DateHelper.getCurrentDateTime()
        orders.verified = shopData?.verified!!
        orders.totalPrice = shopData.priceTawar
        orders.status = AppConstants.STATUS_DIPROSES
        pilihMitraViewModel.updateOrderData(orders).observe(viewLifecycleOwner, { orderData ->
            if (orderData != null) {
                Log.d("sendNotif", "true")
                sendNotif()
            } else {
                progressDialog.dismiss()
                dialog?.dismiss()
            }
        })
    }

    private fun sendNotif() {
        val notif = Notifications(
            body = AppConstants.MESSAGE_STATUS_PESANAN,
            date = DateHelper.getCurrentDateTime(),
            orderId = orders?.orderId,
            receiverId = orders?.shopId,
            receiverPicture = orders?.shopPicture,
            title = AppConstants.TITLE_STATUS_PESANAN,
            senderId = orders?.userId,
            senderPicture = orders?.userPicture
        )
        pilihMitraViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
            if (status == AppConstants.STATUS_SUCCESS) {
                progressDialog.dismiss()
                Toast.makeText(
                    requireContext(),
                    "Pesanan berhasil diproses, silahkan lihat status pesanan anda di halaman Transaksi",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().onBackPressed()
                dialog?.dismiss()
            } else {
                progressDialog.dismiss()
                dialog?.dismiss()
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        })
    }

}