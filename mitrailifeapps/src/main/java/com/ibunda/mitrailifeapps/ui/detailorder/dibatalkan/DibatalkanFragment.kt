package com.ibunda.mitrailifeapps.ui.detailorder.dibatalkan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.data.model.Users
import com.ibunda.mitrailifeapps.databinding.FragmentDibatalkanBinding
import com.ibunda.mitrailifeapps.ui.detailorder.DetailViewModel
import com.ibunda.mitrailifeapps.ui.maps.MapsActivity
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DibatalkanFragment : Fragment() {

    private lateinit var binding : FragmentDibatalkanBinding

    private val detailViewModel: DetailViewModel by activityViewModels()
    private lateinit var ordersData: Orders
    private lateinit var userDataProfile: Users

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDibatalkanBinding.inflate(inflater, container, false)
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
            tvCanceledAt.text = ordersData.canceledAt
            tvDate.text = ordersData.orderDate
            tvTimeOrder.text = ordersData.orderTime
            tvLokasi.text = ordersData.address
            tvMetodePembayaran.text = ordersData.payment
            tvDibatalkanOleh.text = ordersData.canceledBy
            tvAlasanDibatalkan.text = ordersData.canceledReason
        }
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnLihatLokasi.setOnClickListener {
            openMaps()
        }
    }

    private fun openMaps() {
        //getUserData
        detailViewModel.getUserProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    //openMaps
                    val intent =
                        Intent(requireActivity(), MapsActivity::class.java)
                    intent.putExtra(MapsActivity.EXTRA_USER_MAPS, userDataProfile)
                    startActivity(intent)
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }
}