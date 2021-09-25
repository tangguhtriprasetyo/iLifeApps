package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.selesai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.FragmentSelesaiBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionViewModel
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.selesai.penilaian.PenilaianFragment
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.utils.loadImage


class SelesaiFragment : Fragment() {

    private lateinit var binding: FragmentSelesaiBinding

    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private lateinit var orderData: Orders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSelesaiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionViewModel.getOrderData()
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    orderData = orders
                    if (orderData.orderKhusus) {
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
        binding.btnNilaiUlasan.setOnClickListener {
            val mFragmentManager = parentFragmentManager
            val mPenilaianFragment = PenilaianFragment()
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_detail_activity,
                    mPenilaianFragment
                )
            }
        }
        binding.btnPesan.setOnClickListener {
            val intent =
                Intent(requireActivity(), ListMitraActivity::class.java)
            intent.putExtra(ListMitraActivity.EXTRA_SHOP, orderData.shopId)
            intent.putExtra(ListMitraActivity.EXTRA_ORDER_AGAIN, true)
            intent.putExtra(ListMitraActivity.EXTRA_USER, orderData.userId)
            startActivity(intent)
        }
        binding.btnLihatProfil.setOnClickListener {
            val intent =
                Intent(requireActivity(), ListMitraActivity::class.java)
            intent.putExtra(ListMitraActivity.EXTRA_SHOP, orderData.shopId)
            intent.putExtra(ListMitraActivity.EXTRA_TRANSACTION, true)
            intent.putExtra(ListMitraActivity.EXTRA_USER, orderData.userId)
            startActivity(intent)
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
            tvProcessedAt.text = orderData.processedAt
            tvStartAt.text = orderData.startAt
            tvArrivedAt.text = orderData.arrivedAt
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
            //shopData
            if (orderData.verified) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfile.loadImage(orderData.shopPicture)
            tvNamaMitra.text = orderData.shopName
            tvKategoriMitra.text = orderData.categoryName
            //scheduleOrder
            tvCreatedAt.text = orderData.createdAt
            tvProcessedAt.text = orderData.processedAt
            tvStartAt.text = orderData.startAt
            tvArrivedAt.text = orderData.arrivedAt
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
            //button
            btnPesan.visibility = View.GONE
        }
    }

}