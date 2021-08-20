package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.selesai.penilaian

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.databinding.FragmentPenilaianBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.TransactionViewModel
import com.ibunda.ilifeapps.utils.loadImage

class PenilaianFragment : Fragment() {

    private lateinit var binding : FragmentPenilaianBinding

    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private lateinit var orderData: Orders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPenilaianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionViewModel.getOrderData()
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    orderData = orders
                    setDataShop(orderData)
                }
                Log.d("ViewModelOrder: ", orders.toString())
            })

        initOnClick()


    }

    private fun setDataShop(orderData: Orders) {
        with(binding) {
            if (orderData.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            imgProfile.loadImage(orderData.shopPicture)
            tvNamaMitra.text = orderData.shopName
            tvKategoriMitra.text = orderData.categoryName
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
        val valueRating: Float = binding.ratingBar.rating
        Log.e(valueRating.toString(), "valueRating")
    }

}