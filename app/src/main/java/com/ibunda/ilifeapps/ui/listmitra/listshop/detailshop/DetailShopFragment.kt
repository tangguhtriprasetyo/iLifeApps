package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop

import android.content.Intent
import android.graphics.Paint
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayoutMediator
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentDetailShopBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraViewModel
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.dialogtawarmitra.DialogTawarMitraFragment
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.payment.PaymentFragment
import com.ibunda.ilifeapps.ui.maps.MapsActivity
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import com.ibunda.ilifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.roundToInt

class DetailShopFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDetailShopBinding
    private val listMitraViewModel: ListMitraViewModel by activityViewModels()
    private var shopData: Shops? = null
    private var userData: Users? = null

    companion object {
        const val EXTRA_SHOP_DATA = "extra_shop_data"
        const val FROM_TRANSACTION = "from_transaction"
    }

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

        shopData = Shops()
        if (arguments != null) {
            shopData = requireArguments().getParcelable(EXTRA_SHOP_DATA)
            listMitraViewModel.setShopData(shopData!!.shopId.toString())
                .observe(viewLifecycleOwner, { shops ->
                    if (shops != null) {
                        shopData = shops
                        Log.e(shops.price.toString(), "detailShopPrice")
                    }
                })

            if (requireArguments().getBoolean(FROM_TRANSACTION)) {
                binding.linearPilihanMitra.visibility = View.GONE
            } else {
                binding.linearPilihanMitra.visibility = View.VISIBLE
            }

        }

        initView()
        initTabLayout()
        getShopData()

    }

    private fun initTabLayout() {
        val sectionAdapter = SectionDetailAdapter(this)
        binding.viewPager.adapter = sectionAdapter
        binding.viewPager.isSaveEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Kemampuan"
                1 -> tab.text = "Ulasan"
            }
        }.attach()
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
            R.id.ic_back -> activity?.onBackPressed()
            R.id.btn_pesan -> gotoPayment()
            R.id.ic_instagram -> openInstagram(shopData?.instagram!!)
            R.id.ic_facebook -> openFb(shopData?.facebook!!)
            R.id.btn_tawar -> showDialogTawar()
            R.id.btn_lihat_lokasi -> openMaps()
        }
    }

    private fun openMaps() {
        val intent =
            Intent(requireActivity(), MapsActivity::class.java)
        intent.putExtra(MapsActivity.EXTRA_SHOP_MAPS, shopData)
        startActivity(intent)
    }

    private fun openFb(facebook: String) {
        val url = "https://www.facebook.com/$facebook"
        val uriUrl = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    private fun openInstagram(instagram: String) {
        val url = "https://www.instagram.com/$instagram"
        val uriUrl = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    private fun gotoPayment() {
        val mFragmentManager = parentFragmentManager
        val mPaymentFragment = PaymentFragment()
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_listshop_activity,
                mPaymentFragment
            )
        }
    }

    @ExperimentalCoroutinesApi
    private fun showDialogTawar() {
        val mDialogTawarMitraFragment = DialogTawarMitraFragment()
        val mBundle = Bundle()
        mBundle.putParcelable(DialogTawarMitraFragment.EXTRA_USER, userData)
        mBundle.putParcelable(DialogTawarMitraFragment.EXTRA_SHOP, shopData)
        mDialogTawarMitraFragment.arguments = mBundle
        mDialogTawarMitraFragment.show(
            requireActivity().supportFragmentManager,
            DialogTawarMitraFragment::class.java.simpleName
        )
    }

    private fun getShopData() {
        listMitraViewModel.getShopData()
            .observe(viewLifecycleOwner, { shops ->
                if (shops != null) {
                    shopData = shops
                    setShopData(shopData!!)

                    listMitraViewModel.getProfileData()
                        .observe(viewLifecycleOwner, { users ->
                            if (users != null) {
                                userData = users
                                setLocationDistance()
                            }
                        })
                }
            })
    }

    private fun setLocationDistance() {
        if (userData!!.latitude != null && userData!!.longitude != null) {
            val userLocation = Location("userLocation")
            userLocation.latitude = userData!!.latitude!!
            userLocation.longitude = userData!!.longitude!!

            val shopLocation = Location("shopLocation")
            shopLocation.latitude = shopData!!.latitude!!
            shopLocation.longitude = shopData!!.longitude!!

            var distance: Int = (userLocation.distanceTo(shopLocation)).roundToInt()
            if (distance >= 1000) {
                val distanceText = "${distance.div(1000)} Km"
                binding.tvJarakMitra.text = distanceText
            } else {
                val distanceText = "$distance Meter"
                binding.tvJarakMitra.text = distanceText
            }

        } else {
            binding.tvJarakMitra.text = "-"
        }
    }

    private fun setShopData(shopData: Shops) {
        with(binding) {
            imgProfileMitra.loadImage(shopData.shopPicture)
            tvNamaMitra.text = shopData.shopName
            if (shopData.verified == true) {
                icVerified.visibility = View.VISIBLE
            }

            if (shopData.promo == true) {
                tvHargaMitraSebelum.visibility = View.VISIBLE
                tvHargaMitraSebelum.text = PriceFormatHelper.getPriceFormat(shopData.price)
                tvHargaMitraSebelum.paintFlags =
                    tvHargaMitraSebelum.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvHargaMitra.text = PriceFormatHelper.getPriceFormat(shopData.shopPromo)
            } else {
                tvHargaMitra.text = PriceFormatHelper.getPriceFormat(shopData.price)
            }

            tvNilaiRating.text = shopData.rating.toString()
            tvJumlahUlasanMitra.text = shopData.totalUlasan.toString() + " Ulasan"
            tvKategoriMitra.text = shopData.categoryName
            tvMitraBergabung.text = "Bergabung sejak " + shopData.registeredAt
            tvLokasiMitra.text = shopData.address
        }
    }

}