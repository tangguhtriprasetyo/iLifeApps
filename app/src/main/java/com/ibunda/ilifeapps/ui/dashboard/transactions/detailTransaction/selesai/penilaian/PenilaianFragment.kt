package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.selesai.penilaian

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Ulasan
import com.ibunda.ilifeapps.databinding.FragmentPenilaianBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionViewModel
import com.ibunda.ilifeapps.utils.AppConstants
import com.ibunda.ilifeapps.utils.DateHelper
import com.ibunda.ilifeapps.utils.loadImage
import kotlin.math.round

class PenilaianFragment : Fragment() {

    private lateinit var binding : FragmentPenilaianBinding

    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private  var shopData = Shops()
    private  var ordersData = Orders()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPenilaianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionViewModel.getOrderData()
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    ordersData = orders
                    orders.shopId?.let { data ->
                        transactionViewModel.setShopData(data).observe(viewLifecycleOwner, {
                            if (it != null) {
                                shopData = it
                                setDataShop(shopData)
                            }
                        })
                    }
                }
                Log.d("ViewModelOrder: ", orders.toString())
            })

        initOnClick()

    }

    private fun setDataShop(shopData: Shops) {
        with(binding) {
            if (shopData.verified) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfile.loadImage(shopData.shopPicture)
            tvNamaMitra.text = shopData.shopName
            tvKategoriMitra.text = shopData.categoryName
        }
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnKirimUlasan.setOnClickListener {
            sendUlasan()
        }
    }

    private fun sendUlasan() {
        val totalRating = shopData.totalRating?.plus(binding.ratingBar.rating)
        val totalUlasan = shopData.totalUlasan?.plus(1)
        var rating = totalRating?.div(totalUlasan?.toDouble()!!)
        if (rating != null) {
            rating = round(rating * 100).div(100)
        }
        val ulasan = Ulasan(
            date = DateHelper.getCurrentDate(),
            rating = binding.ratingBar.rating.toDouble(),
            shopId = shopData.shopId,
            shopName = shopData.shopName,
            shopPicture = shopData.shopPicture,
            ulasan = binding.etUlasan.text.toString().trim(),
            userId = ordersData.userId,
            userName = ordersData.userName,
            userPicture = ordersData.userPicture,
        verified = shopData.verified
            )
        transactionViewModel.uploadPenilaian(ulasan, rating!!, totalRating!!)
            .observe(viewLifecycleOwner, { status ->

                if (status == AppConstants.STATUS_SUCCESS) {
                    Toast.makeText(
                        requireContext(),
                        "Penilaian berhasil dilakukan. Terima kasih telah menilai mitra kami.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.onBackPressed()
                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }

        })
    }

}