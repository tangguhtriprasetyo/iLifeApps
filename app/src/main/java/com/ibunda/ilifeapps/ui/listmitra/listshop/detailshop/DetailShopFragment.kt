package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.databinding.FragmentDetailShopBinding
import com.ibunda.ilifeapps.utils.loadImage
import java.text.NumberFormat
import java.util.*

class DetailShopFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentDetailShopBinding
    private val detailShopViewModel: DetailShopViewModel by activityViewModels()
    private lateinit var shopData: Shops

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        getShopData()

    }

    private fun initView() {
        binding.icBack.setOnClickListener(this)
        binding.btnLihatLokasi.setOnClickListener(this)
        binding.icFacebook.setOnClickListener(this)
        binding.icInstagram.setOnClickListener(this)
        binding.btnTawar.setOnClickListener(this)
        binding.btnPesan.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_back -> requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }


    private fun getShopData() {
        detailShopViewModel.getShopData()
            .observe(viewLifecycleOwner, { shops ->
                if (shops != null) {
                    shopData = shops
                    setShopData(shopData)
                }
                Log.d("ViewModelProfile: ", shops.toString())
            })
    }

    private fun setShopData(shopData: Shops) {
        with(binding) {
            imgProfileMitra.loadImage(shopData.shopPicture)
            tvNamaMitra.text = shopData.shopName
            if (shopData.verified == true) {
                icVerified.visibility = View.VISIBLE
            }
            tvNilaiRating.text = shopData.rating.toString()
            tvJumlahUlasanMitra.text = shopData.totalUlasan.toString() + " Ulasan"
            val localeId = Locale("in", "ID")
            val priceFormat = NumberFormat.getCurrencyInstance(localeId)
            tvHargaMitra.text = priceFormat.format(shopData.price)
            tvKategoriMitra.text = shopData.categoryName
            tvMitraBergabung.text = "Bergabung sejak " + shopData.registeredAt
            tvLokasiMitra.text = shopData.address
        }
    }

}