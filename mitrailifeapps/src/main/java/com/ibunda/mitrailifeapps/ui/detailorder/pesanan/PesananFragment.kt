package com.ibunda.mitrailifeapps.ui.detailorder.pesanan

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.mitrailifeapps.data.model.Notifications
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.FragmentPesananBinding
import com.ibunda.mitrailifeapps.ui.detailorder.DetailViewModel
import com.ibunda.mitrailifeapps.ui.detailorder.pesanan.dialogtolakpesanan.DialogTolakPesananFragment
import com.ibunda.mitrailifeapps.utils.AppConstants
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_DIBATALKAN
import com.ibunda.mitrailifeapps.utils.AppConstants.STATUS_DIPROSES
import com.ibunda.mitrailifeapps.utils.DateHelper
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PesananFragment : Fragment() {

    private lateinit var binding : FragmentPesananBinding

    private val detailViewModel: DetailViewModel by activityViewModels()
    private lateinit var ordersData: Orders

    private var reasonCancel: String? = null
    private lateinit var progressDialog : Dialog

    companion object {
        const val PESANAN_DIPROSES = "pesanan_diproses"
        const val PESANAN_DIBATALKAN = "pesanan_dibatalkan"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())
        getOrdersData()
        initOnClick()
    }

    private fun getOrdersData() {
        detailViewModel.getOrderData()
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    ordersData = orders
                    initViewOrder(ordersData)
                }
                Log.d("ViewModelOrder: ", orders.toString())
            })
    }

    private fun initViewOrder(ordersData: Orders) {
        with(binding) {
            tvTotalHargaJasa.text = ordersData.totalPrice
            imgProfileUser.loadImage(ordersData.userPicture)
            tvNamaUser.text = ordersData.userName
            tvCreatedAt.text = ordersData.createdAt
            tvDate.text = ordersData.orderDate
            tvTimeOrder.text = ordersData.orderTime
            tvLokasi.text = ordersData.address
            tvMetodePembayaran.text = ordersData.payment
        }
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnTerimaPekerjaan.setOnClickListener {
            orderProcessed()
        }
        binding.btnTolakPekerjaan.setOnClickListener {
            val mDialogBatalkanPesananFragment = DialogTolakPesananFragment()
            val mFragmentManager = childFragmentManager
            mDialogBatalkanPesananFragment.show(mFragmentManager, DialogTolakPesananFragment::class.java.simpleName)
        }
    }

    private fun orderProcessed() {
        progressDialog.show()
        ordersData.processedAt = DateHelper.getCurrentDateTime()
        ordersData.status = STATUS_DIPROSES

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                sendNotif(PESANAN_DIPROSES)
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Pesanan berhasil diproses, silahkan cek di Transaksi", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Update Order Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    internal var optionDialogListener: DialogTolakPesananFragment.OnOptionDialogListener = object : DialogTolakPesananFragment.OnOptionDialogListener {
        override fun onOptionChosen(reason: String?) {
            reasonCancel = reason
            cancelOrder(reasonCancel)
        }
    }

    private fun cancelOrder(reasonCancel: String?) {
        progressDialog.show()
        ordersData.canceledReason = reasonCancel
        ordersData.canceledBy = "Mitra"
        ordersData.canceledAt = DateHelper.getCurrentDateTime()
        ordersData.status = STATUS_DIBATALKAN

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                sendNotif(PESANAN_DIBATALKAN)
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Pesanan berhasil dibatalkan karena $reasonCancel", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Update Order Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun sendNotif(result: String) {
        val notif = Notifications(
            date = DateHelper.getCurrentDateTime(),
            orderId = ordersData.orderId,
            receiverId = ordersData.userId,
            receiverPicture = ordersData.userPicture,
            senderId = ordersData.shopId,
            senderPicture = ordersData.shopPicture
        )
        if (result == PESANAN_DIPROSES) {
            notif.body = AppConstants.BODY_STATUS_DIPROSES
            notif.title = AppConstants.TITLE_STATUS_DIPROSES
            detailViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    Toast.makeText(
                        requireContext(),
                        AppConstants.TOAST_STATUS_DIPROSES,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            notif.body = AppConstants.BODY_STATUS_DIBATALKAN
            notif.title = AppConstants.TITLE_STATUS_DIBATALKAN
            detailViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    Toast.makeText(
                        requireContext(),
                        AppConstants.TOAST_STATUS_DIPROSES,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


}