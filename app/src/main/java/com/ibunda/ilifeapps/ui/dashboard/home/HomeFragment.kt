package com.ibunda.ilifeapps.ui.dashboard.home

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
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentHomeBinding
import com.ibunda.ilifeapps.ui.dashboard.MainActivity
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.ui.dashboard.home.customorder.CustomOrderFragment
import com.ibunda.ilifeapps.ui.dashboard.home.dialogeditprofile.DialogEditProfileFragment
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.dialogbatalkanpesanan.DialogBatalkanPesananFragment
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.dialogtawarmitra.DialogTawarMitraFragment

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentHomeBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var userDataProfile: Users

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kategoriElektronik.setOnClickListener(this)
        binding.kategoriKebersihan.setOnClickListener(this)
        binding.kategoriKesehatan.setOnClickListener(this)
        binding.kategoriTukang.setOnClickListener(this)
        binding.kategoriGuruLes.setOnClickListener(this)
        binding.kategoriLainnya.setOnClickListener(this)

        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    if (userDataProfile.isNew == true) {
                        showDialogEditProfile()
                    }
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

    override fun onClick(v: View) {
        var categoryName: String? = null
        when (v.id) {
                R.id.kategori_elektronik -> gotoListShop("Servis Elektronik")
                R.id.kategori_kebersihan -> gotoListShop("Kebersihan")
                R.id.kategori_kesehatan -> gotoListShop("Asisten Kesehatan")
                R.id.kategori_tukang -> gotoListShop("Tukang")
                R.id.kategori_guru_les -> gotoListShop("Guru Les")
                R.id.kategori_lainnya -> gotoCustomOrder()
        }
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

    private fun gotoListShop(categoryName: String) {
        val intent =
            Intent(requireActivity(), ListMitraActivity::class.java)
        intent.putExtra(ListMitraActivity.EXTRA_CATEGORY_NAME, categoryName)
        startActivity(intent)
    }

    private fun showDialogTawar() {
        val mDialogTawarMitraFragment = DialogTawarMitraFragment()
        mDialogTawarMitraFragment.show(
            requireActivity().supportFragmentManager,
            DialogTawarMitraFragment::class.java.simpleName
        )
    }

    private fun showDialogCancelOrder() {
        val mDialogBatalkanPesananFragment = DialogBatalkanPesananFragment()
        mDialogBatalkanPesananFragment.show(
            requireActivity().supportFragmentManager,
            DialogBatalkanPesananFragment::class.java.simpleName
        )
    }

    private fun showDialogEditProfile() {
        val mFragmentManager = parentFragmentManager
        val mDialogEditProfileFragment = DialogEditProfileFragment()
        mDialogEditProfileFragment.isCancelable = false
        mDialogEditProfileFragment.show(mFragmentManager, "DialogEditProfileFragment")
    }
}