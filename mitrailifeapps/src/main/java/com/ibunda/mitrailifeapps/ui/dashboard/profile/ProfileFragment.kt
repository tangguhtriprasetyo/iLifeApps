package com.ibunda.mitrailifeapps.ui.dashboard.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Mitras
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentProfileBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainActivity
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.chats.ChatsActivity
import com.ibunda.mitrailifeapps.ui.dashboard.notifications.NotificationsFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopOneFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.dialogkelolashop.DialogKelolaShopFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.editshop.EditShopFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.setting.SettingFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.ulasan.UlasanFragment
import com.ibunda.mitrailifeapps.ui.maps.MapsActivity
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ProfileFragment : Fragment(), View.OnClickListener  {

    private lateinit var binding : FragmentProfileBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var mitraDataProfile: Mitras
    private lateinit var shopsDataProfile: Shops

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMitraData()
    }

    private fun getMitraData() {
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    mitraDataProfile = userProfile
                    if (mitraDataProfile.totalShop == 0) {
                        initEmptyShop()
                    } else {
                        initShops()
                    }
                }
                Log.d("ViewModelMitraProfile: ", userProfile.toString())
            })
    }

    private fun initEmptyShop() {
        binding.linearEmptyToko.visibility = View.VISIBLE
        binding.linearToko.visibility = View.GONE
        binding.chat.icMessage.visibility = View.GONE
        binding.chat.imgBadgeChat.visibility = View.GONE
        binding.notification.icNotification.visibility = View.GONE
        binding.notification.imgBadgeNotification.visibility = View.GONE
        binding.icSetting.setOnClickListener(this)

        binding.btnTambahToko.setOnClickListener {
            val mCreateShopOneFragment = CreateShopOneFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.commit {
                addToBackStack(null)
                replace(
                    R.id.host_fragment_activity_main,
                    mCreateShopOneFragment
                )
            }
        }
    }

    private fun initShops() {

        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    setDataShops(shopsDataProfile)
                    listenNotif(shopsDataProfile.shopId)
                    listenChats(shopsDataProfile.shopId)
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })

        binding.notification.icNotification.setOnClickListener(this)
        binding.chat.icMessage.setOnClickListener(this)
        binding.icSetting.setOnClickListener(this)
        binding.linearDeskripsiToko.setOnClickListener(this)
        binding.linearEditAkun.setOnClickListener(this)
        binding.linearUlasanToko.setOnClickListener(this)
        binding.btnKelolaToko.setOnClickListener(this)
        binding.linearLokasiToko.setOnClickListener(this)
    }

    private fun listenChats(shopId: String?) {
        mainViewModel.getListChatRoom(shopId.toString())
            .observe(viewLifecycleOwner, { listChats ->
                if (listChats != null && listChats.isNotEmpty()) {
                    binding.chat.imgBadgeChat.visibility = View.VISIBLE
                } else {
                    binding.chat.imgBadgeChat.visibility = View.GONE
                }
            })
    }

    private fun listenNotif(shopId: String?) {
        mainViewModel.getListNotif(shopId.toString())
            .observe(viewLifecycleOwner, { listNotif ->
                if (listNotif != null && listNotif.isNotEmpty()) {
                    binding.notification.imgBadgeNotification.visibility = View.VISIBLE
                } else {
                    binding.notification.imgBadgeNotification.visibility = View.GONE
                }
            })
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {

                    mainViewModel.getShopData()
                        .observe(viewLifecycleOwner, { shopsProfile ->
                            if (shopsProfile != null) {
                                shopsDataProfile = shopsProfile
                                shopsDataProfile.address = result.data?.getStringExtra("address")
                                shopsDataProfile.latitude = result.data?.getDoubleExtra("latitude", 0.0)
                                shopsDataProfile.longitude = result.data?.getDoubleExtra("longitude", 0.0)
                                updateShopLocation(shopsProfile)
                            }
                            Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
                        })


                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    val status = result.data?.let { Autocomplete.getStatusFromIntent(it) }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }

    private fun updateShopLocation(shopsProfile: Shops) {

    }

    private fun setDataShops(shopsDataProfile: Shops) {
        with(binding) {
            linearToko.visibility = View.VISIBLE

            imgProfileToko.loadImage(shopsDataProfile.shopPicture)
            tvNamaToko.text = shopsDataProfile.shopName
            tvBergabungSejak.text = shopsDataProfile.registeredAt
            tvKategoriToko.text = shopsDataProfile.categoryName
            tvRatingMitra.text = shopsDataProfile.rating.toString()
            tvTotalPesanan.text = shopsDataProfile.totalPesananSukses.toString()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_message -> gotoChats()
            R.id.ic_notification -> gotoNotifications()
            R.id.ic_setting -> openSetting()
            R.id.linear_deskripsi_toko -> editAkun("EditKemampuan")
            R.id.linear_edit_akun -> editAkun("EditShop")
            R.id.linear_ulasan_toko -> openUlasan()
            R.id.btn_kelola_toko -> kelolaToko()
            R.id.linear_lokasi_toko -> openMaps()
        }
    }

    private fun gotoNotifications() {
        val mFragmentManager = parentFragmentManager
        val mCustomOrderFragment = NotificationsFragment()
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_fragment_activity_main,
                mCustomOrderFragment,
                MainActivity.CHILD_FRAGMENT
            )
        }
    }

    private fun gotoChats() {
        val intent = Intent(requireActivity(), ChatsActivity::class.java)
        intent.putExtra(ChatsActivity.EXTRA_USER, shopsDataProfile)
        startActivity(intent)
    }

    private fun openMaps() {
        val intent =
            Intent(requireActivity(), MapsActivity::class.java)
        intent.putExtra(MapsActivity.EXTRA_USER_MAPS, shopsDataProfile)
        intent.putExtra(MapsActivity.EXTRA_SHOPS_DATA, true)
        startActivity(intent)
    }

    private fun kelolaToko() {
        val mDialogKelolaShopFragment = DialogKelolaShopFragment()
        val mFragmentManager = parentFragmentManager
        mDialogKelolaShopFragment.show(mFragmentManager, DialogKelolaShopFragment::class.java.simpleName)
    }

    private fun openUlasan() {
        val mUlasanFragment = UlasanFragment()
        val mBundle = Bundle()
        mBundle.putBoolean(UlasanFragment.FROM_TRANSACTION, false)
        mUlasanFragment.arguments = mBundle
        setCurrentFragment(mUlasanFragment)
    }

    private fun editAkun(value: String) {
        val mEditShopFragment = EditShopFragment()
        val mBundle = Bundle()
        mBundle.putString(EditShopFragment.EXTRA_EDIT, value)
        mEditShopFragment.arguments = mBundle
        setCurrentFragment(mEditShopFragment)
    }

    private fun openSetting() {
        val mSettingFragment = SettingFragment()
        setCurrentFragment(mSettingFragment)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.host_fragment_activity_main, fragment, MainActivity.CHILD_FRAGMENT)
        }
    }

    private fun openNotifications() {

    }

    private fun openMessage() {
    }

    override fun onResume() {
        super.onResume()
        getMitraData()
    }

    override fun onStart() {
        super.onStart()
        getMitraData()
    }

}