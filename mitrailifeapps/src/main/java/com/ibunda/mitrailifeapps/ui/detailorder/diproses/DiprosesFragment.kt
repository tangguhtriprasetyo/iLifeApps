package com.ibunda.mitrailifeapps.ui.detailorder.diproses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.FragmentDiprosesBinding
import com.ibunda.mitrailifeapps.databinding.ItemDialogFinishOrderBinding
import com.ibunda.mitrailifeapps.ui.detailorder.DetailViewModel
import com.ibunda.mitrailifeapps.utils.AppConstants
import com.ibunda.mitrailifeapps.utils.DateHelper
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DiprosesFragment : Fragment() {

    private lateinit var binding : FragmentDiprosesBinding

    private val detailViewModel: DetailViewModel by activityViewModels()
    private lateinit var ordersData: Orders

    companion object {
        const val MULAI_PERJALANAN = "Mulai Perjalanan"
        const val SAMPAI_TUJUAN = "Sampai Tujuan"
        const val PESANAN_SELESAI = "Pesanan Selesai"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiprosesBinding.inflate(inflater, container, false)
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
            tvProcessedAt.text = ordersData.processedAt
            btnStatusPekerjaan.text = MULAI_PERJALANAN
            if (ordersData.startAt!= null) {
                tvInfoMulaiPerjalanan.visibility = View.VISIBLE
                tvStartAt.visibility = View.VISIBLE
                tvStartAt.text = ordersData.startAt
                btnStatusPekerjaan.text = SAMPAI_TUJUAN
            }
            if (ordersData.arrivedAt!= null) {
                tvInfoSampaiTujuan.visibility = View.VISIBLE
                tvArrivedAt.visibility = View.VISIBLE
                tvArrivedAt.text = ordersData.arrivedAt
                btnStatusPekerjaan.text = PESANAN_SELESAI
            }
            tvDate.text = ordersData.orderDate
            tvTimeOrder.text = ordersData.orderTime
            tvLokasi.text = ordersData.address
            tvMetodePembayaran.text = ordersData.payment

            val btnStatus = btnStatusPekerjaan.getText().toString()
            Log.e(btnStatus, "statusButton")
            if (btnStatus == MULAI_PERJALANAN) {
                btnStatusPekerjaan.setOnClickListener { mulaiPerjalanan() }
            } else if (btnStatus == SAMPAI_TUJUAN) {
                btnStatusPekerjaan.setOnClickListener{ sampaiTujuan() }
            } else if (btnStatus == PESANAN_SELESAI) {
                btnStatusPekerjaan.setOnClickListener{ dialogFinishOrder() }
            }
        }
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun dialogFinishOrder() {
        val builder = AlertDialog.Builder(requireContext())
        val binding : ItemDialogFinishOrderBinding = ItemDialogFinishOrderBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.show()
        binding.btnTidak.setOnClickListener { dialog.dismiss() }
        binding.btnYa.setOnClickListener { finishOrder() }
    }

    private fun finishOrder() {
        progressDialog(true)
        ordersData.finishedAt = DateHelper.getCurrentDateTime()
        ordersData.status = AppConstants.STATUS_SELESAI

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                progressDialog(false)
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

    private fun sampaiTujuan() {
        progressDialog(true)
        ordersData.arrivedAt = DateHelper.getCurrentDateTime()

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                progressDialog(false)
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

    private fun mulaiPerjalanan() {
        progressDialog(true)
        ordersData.startAt = DateHelper.getCurrentDateTime()

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                progressDialog(false)
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