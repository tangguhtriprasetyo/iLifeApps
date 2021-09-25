package com.ibunda.ilifeapps.ui.dashboard.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentHomeBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.ui.dashboard.home.ads.AdsHomeAdapter
import com.ibunda.ilifeapps.ui.dashboard.home.chats.ChatsActivity
import com.ibunda.ilifeapps.ui.dashboard.home.customorder.CustomOrderFragment
import com.ibunda.ilifeapps.ui.dashboard.home.dialogeditprofile.DialogEditProfileFragment
import com.ibunda.ilifeapps.ui.dashboard.home.notifications.NotificationsFragment
import com.ibunda.ilifeapps.ui.dashboard.home.story.StoryHomeAdaper
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.ui.maps.MapsActivity
import com.ibunda.ilifeapps.utils.ProgressDialogHelper
import com.ibunda.ilifeapps.utils.RvDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var progressDialog: Dialog

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var userDataProfile: Users
    private val adsHomeAdapter = AdsHomeAdapter()
    private val storyHomeAdapter = StoryHomeAdaper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())
        progressDialog.show()
        getProfileData()
        initView()

    }

    private fun listenNotif() {
        mainViewModel.getListNotif(userDataProfile.userId.toString())
            .observe(viewLifecycleOwner, { listNotif ->
                if (listNotif != null && listNotif.isNotEmpty()) {
                    binding.notification.imgBadgeNotification.visibility = View.VISIBLE
                } else {
                    binding.notification.imgBadgeNotification.visibility = View.GONE
                }
                progressDialog.dismiss()
            })
    }

    private fun listenChats() {
        mainViewModel.getListChatRoom(userDataProfile.userId.toString())
            .observe(viewLifecycleOwner, { listChats ->
                if (listChats != null && listChats.isNotEmpty()) {
                    binding.chat.imgBadgeChat.visibility = View.VISIBLE
                } else {
                    binding.chat.imgBadgeChat.visibility = View.GONE
                }
            })
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {

                    userDataProfile = userProfile
                    if (userDataProfile.address != null) {
                        binding.etLocation.hint = userDataProfile.address
                    } else {
                        binding.etLocation.hint = "Tentukan Lokasi Anda"
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.kategori_elektronik -> gotoListShop("Servis Elektronik", userDataProfile)
            R.id.kategori_kebersihan -> gotoListShop("Kebersihan", userDataProfile)
            R.id.kategori_kesehatan -> gotoListShop("Asisten Kesehatan", userDataProfile)
            R.id.kategori_tukang -> gotoListShop("Tukang", userDataProfile)
            R.id.kategori_guru_les -> gotoListShop("Guru Les", userDataProfile)
            R.id.kategori_lainnya -> gotoCustomOrder()
            R.id.et_location -> openMaps()
            R.id.ic_message -> gotoChats()
            R.id.ic_notification -> gotoNotifications()
        }
    }

    private fun setRvStoryHome() {
        mainViewModel.getListAds("adsStory").observe(viewLifecycleOwner, { listStory ->
            if (listStory != null) {
                storyHomeAdapter.setListStory(listStory)
                setStoryAdapter()
            }
        })
    }

    private fun setStoryAdapter() {
        with(binding.rvStoryMitra) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = storyHomeAdapter
            Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun setRvAdsHome() {
        mainViewModel.getListAds("adsHome").observe(viewLifecycleOwner, { listAds ->
            if (listAds != null) {
                adsHomeAdapter.setListAds(listAds)
                setAdsAdapter()
            }
        })
    }

    private fun setAdsAdapter() {
        with(binding.rvAdsHome) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            val snapHelper: SnapHelper = PagerSnapHelper()
            onFlingListener = null
            snapHelper.attachToRecyclerView(this)
            val mRvDecoration = RvDecoration()
            addItemDecoration(mRvDecoration)
            adapter = adsHomeAdapter
            Log.d(android.content.ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun getProfileData() {
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {

                    userDataProfile = userProfile

                    if (userDataProfile.address != null) {
                        binding.etLocation.hint = userDataProfile.address
                    } else {
                        binding.etLocation.hint = "Tentukan Lokasi Anda"
                    }

                    if (userDataProfile.isNew == true) {
                        showDialogEditProfile()
                    }
                    setRvAdsHome()
                    setRvStoryHome()
                    listenChats()
                    listenNotif()
                }
            })
    }

    private fun initView() {

        binding.kategoriElektronik.setOnClickListener(this)
        binding.kategoriKebersihan.setOnClickListener(this)
        binding.kategoriKesehatan.setOnClickListener(this)
        binding.kategoriTukang.setOnClickListener(this)
        binding.kategoriGuruLes.setOnClickListener(this)
        binding.kategoriLainnya.setOnClickListener(this)
        binding.etLocation.setOnClickListener(this)
        binding.notification.icNotification.setOnClickListener(this)
        binding.chat.icMessage.setOnClickListener(this)
    }

    private fun gotoCustomOrder() {
        val mFragmentManager = parentFragmentManager
        val mCustomOrderFragment = CustomOrderFragment()
        mFragmentManager.commit {
            addToBackStack(null)
            replace(
                R.id.host_fragment_activity_main,
                mCustomOrderFragment,
                MainActivity.CHILD_FRAGMENT
            )
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
        intent.putExtra(ChatsActivity.EXTRA_USER, userDataProfile)
        startActivity(intent)
    }

    private fun gotoListShop(categoryName: String, userDataProfile: Users) {
        val intent =
            Intent(requireActivity(), ListMitraActivity::class.java)
        intent.putExtra(ListMitraActivity.EXTRA_CATEGORY_NAME, categoryName)
        intent.putExtra(MainActivity.EXTRA_USER, userDataProfile.userId)
        startActivity(intent)
    }

    private fun showDialogEditProfile() {
        val mFragmentManager = parentFragmentManager
        val mDialogEditProfileFragment = DialogEditProfileFragment()
        mDialogEditProfileFragment.isCancelable = false
        mDialogEditProfileFragment.show(mFragmentManager, "DialogEditProfileFragment")
    }

    private fun openMaps() {
        val intent =
            Intent(requireActivity(), MapsActivity::class.java)
        intent.putExtra(MapsActivity.EXTRA_USER_MAPS, userDataProfile)
        startActivity(intent)
    }
}