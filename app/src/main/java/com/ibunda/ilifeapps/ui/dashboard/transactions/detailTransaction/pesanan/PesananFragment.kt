package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.FragmentPesananBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionViewModel
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.dialogbatalkanpesanan.DialogBatalkanPesananFragment
import com.ibunda.ilifeapps.utils.loadImage


class PesananFragment : Fragment() {

    private lateinit var binding : FragmentPesananBinding

    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private lateinit var orderData: Orders

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

        getOrderData()
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
        override fun onOptionChosen(category: String?) {
            val kategoriMitra: String? = category
            Toast.makeText(requireContext(), kategoriMitra, Toast.LENGTH_SHORT).show()
        }
    }

}