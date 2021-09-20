package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.firebase.Timestamp
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.FragmentPesananBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionViewModel
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.dialogbatalkanpesanan.DialogBatalkanPesananFragment
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra.PilihMitraFragment
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.DateHelper
import com.ibunda.ilifeapps.utils.loadImage
import java.util.*


class PesananFragment : Fragment() {

    private lateinit var binding: FragmentPesananBinding

    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private lateinit var orderData: Orders
    private var reasonCancel: String? = null

    companion object {
        const val PILIH_MITRA_FRAGMENT_TAG = "pilih_mitra_fragment_tag"
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

        transactionViewModel.getOrderData()
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    orderData = orders
                    if (orderData.orderKhusus == true) {
                        initViewPesananKhusus(orderData)
                    } else {
                        initViewPesananJasa(orderData)
                    }
                }
                Log.d("ViewModelOrder: ", orders.toString())
            })

        initOnClick()
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnBatalkanPesanan.setOnClickListener {
            val mDialogBatalkanPesananFragment = DialogBatalkanPesananFragment()
            val mFragmentManager = childFragmentManager
            mDialogBatalkanPesananFragment.show(mFragmentManager, DialogBatalkanPesananFragment::class.java.simpleName)
        }

        binding.btnUbahMetodePembayaran.setOnClickListener {
            Toast.makeText(requireContext(), "Saat ini hanya tersedia metode pembayaran Bayar di Tempat (COD)", Toast.LENGTH_SHORT).show()
        }

        binding.btnLihatProfil.setOnClickListener {
            val intent =
                Intent(requireActivity(), ListMitraActivity::class.java)
            intent.putExtra(ListMitraActivity.EXTRA_SHOP, orderData.shopId)
            intent.putExtra(ListMitraActivity.EXTRA_TRANSACTION, true)
            intent.putExtra(ListMitraActivity.EXTRA_USER, orderData.userId)
            startActivity(intent)
        }

        binding.btnPilihMitra.setOnClickListener {
            val mFragmentManager = parentFragmentManager
            val mPilihMitraFragment = PilihMitraFragment()

            val mBundle = Bundle()
            mBundle.putParcelable(PilihMitraFragment.EXTRA_ORDER_ID, orderData)
            mPilihMitraFragment.arguments = mBundle

            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_detail_activity,
                    mPilihMitraFragment,
                    PILIH_MITRA_FRAGMENT_TAG
                )
            }
        }
    }

    private fun initViewPesananJasa(orderData: Orders) {
        with(binding) {
            //price
            tvInfoHargaJasa.visibility = View.VISIBLE
            tvTotalHargaJasa.visibility = View.VISIBLE
            tvTotalHargaJasa.text = orderData.totalPrice
            //shopdata
            imgProfile.loadImage(orderData.shopPicture)
            tvNamaMitra.text = orderData.shopName
            tvKategoriMitra.text = orderData.categoryName
            //scheduleOrder
            tvCreatedAt.text = orderData.createdAt
            tvDate.text = orderData.orderDate
            tvTimeOrder.text = orderData.orderTime
            //address
            tvLokasi.text = orderData.address
            //payment
            tvMetodePembayaran.text = orderData.payment
        }
    }

    private fun initViewPesananKhusus(orderData: Orders) {
        with(binding) {
            //price
            tvDanaPesananKhusus.visibility = View.VISIBLE
            tvTotalDanaPesananKhusus.visibility = View.VISIBLE
            tvTotalDanaPesananKhusus.text = orderData.totalPrice
            //userData
            btnLihatProfil.visibility = View.GONE
            imgProfile.loadImage(orderData.userPicture)
            tvNamaMitra.text = orderData.userName
            tvKategoriMitra.text = orderData.categoryName
            //scheduleOrder
            tvCreatedAt.text = orderData.createdAt
            tvDate.text = orderData.orderDate
            tvTimeOrder.text = orderData.orderTime
            //address
            tvLokasi.text = orderData.address
            //job
            linearDeskripsiAll.visibility = View.VISIBLE
            tvValueNamaPekerjaan.text = orderData.jobName
            tvValueDeskripsiPekerjaan.text = orderData.jobDesc
            tvValuePenyediaJasa.text = orderData.jobPerson
            //payment
            tvMetodePembayaran.text = orderData.payment
            //button
            btnPilihMitra.visibility = View.VISIBLE
        }
    }

    internal var optionDialogListener: DialogBatalkanPesananFragment.OnOptionDialogListener = object : DialogBatalkanPesananFragment.OnOptionDialogListener {
        override fun onOptionChosen(reason: String?) {
            reasonCancel = reason
            updateOrderData(reasonCancel)
        }
    }

    private fun updateOrderData(reasonCancel: String?) {
        orderData.canceledReason = reasonCancel
        orderData.canceledBy = "User"
        orderData.canceledAt = DateHelper.getCurrentDateTime()
        orderData.status = AppConstants.STATUS_DIBATALKAN

        transactionViewModel.updateOrderData(orderData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                sendNotif()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Update Order Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun sendNotif() {
        val notif = Notifications(
            notifId = Timestamp(Date()).toString(),
            body = AppConstants.MESSAGE_STATUS_DIBATALKAN,
            date = DateHelper.getCurrentDateTime(),
            orderId = orderData.orderId,
            read = false,
            receiverId = orderData.shopId,
            receiverPicture = orderData.shopPicture,
            title = AppConstants.TITLE_STATUS_DIBATALKAN,
            senderId = orderData.userId,
            senderPicture = orderData.userPicture
        )
        transactionViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
            if (status == AppConstants.STATUS_SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    "Pesanan Telah Dibatalkan, silahkan lihat status pesanan anda di halaman Transaksi",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        })
    }

}