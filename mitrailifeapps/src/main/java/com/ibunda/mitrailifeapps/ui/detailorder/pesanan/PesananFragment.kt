package com.ibunda.mitrailifeapps.ui.detailorder.pesanan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.FragmentPesananBinding
import com.ibunda.mitrailifeapps.ui.detailorder.DetailViewModel
import com.ibunda.mitrailifeapps.ui.detailorder.pesanan.dialogtolakpesanan.DialogTolakPesananFragment
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
        progressDialog(true)
        ordersData.processedAt = DateHelper.getCurrentDateTime()
        ordersData.status = STATUS_DIPROSES

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                progressDialog(false)
                Toast.makeText(requireContext(), "Pesanan berhasil diproses, silahkan cek di Transaksi", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                progressDialog(false)
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
        progressDialog(true)
        ordersData.canceledReason = reasonCancel
        ordersData.canceledBy = "Mitra"
        ordersData.canceledAt = DateHelper.getCurrentDateTime()
        ordersData.status = STATUS_DIBATALKAN

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                progressDialog(false)
                Toast.makeText(requireContext(), "Pesanan berhasil dibatalkan karena $reasonCancel", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                progressDialog(false)
                Toast.makeText(
                    requireActivity(),
                    "Update Order Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun progressDialog(state: Boolean) {
        val dialog = ProgressDialogHelper.setProgressDialog(requireActivity(), "Loading...")
        if (state) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

}