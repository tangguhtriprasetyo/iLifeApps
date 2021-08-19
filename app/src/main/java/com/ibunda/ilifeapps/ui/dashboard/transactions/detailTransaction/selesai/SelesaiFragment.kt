package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.selesai

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.FragmentSelesaiBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionViewModel
import com.ibunda.ilifeapps.utils.loadImage


class SelesaiFragment : Fragment() {

    private lateinit var binding : FragmentSelesaiBinding

    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private lateinit var orderData: Orders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelesaiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getOrderData()
        initOnClick()
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnNilaiUlasan.setOnClickListener {

        }
        binding.btnPesan.setOnClickListener {

        }
        binding.btnLihatProfil.setOnClickListener {

        }
    }

    private fun getOrderData() {
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
            tvInfoPesananDiproses.visibility = View.VISIBLE
            tvProcessedAt.text = orderData.processedAt
            tvInfoMulaiPerjalanan.visibility = View.VISIBLE
            tvStartAt.text = orderData.startAt
            tvInfoSampaiTujuan.visibility = View.VISIBLE
            tvArrivedAt.text = orderData.arrivedAt
            tvInfoPesananSelesai.visibility = View.VISIBLE
            tvFinishedAt.text = orderData.finishedAt
            //
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
            tvInfoPesananDiproses.visibility = View.VISIBLE
            tvProcessedAt.text = orderData.processedAt
            tvInfoMulaiPerjalanan.visibility = View.VISIBLE
            tvStartAt.text = orderData.startAt
            tvInfoSampaiTujuan.visibility = View.VISIBLE
            tvArrivedAt.text = orderData.arrivedAt
            tvInfoPesananSelesai.visibility = View.VISIBLE
            tvFinishedAt.text = orderData.finishedAt
            //
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
        }
    }

}