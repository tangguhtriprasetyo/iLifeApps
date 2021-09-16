package com.ibunda.mitrailifeapps.ui.dashboard.profile.dialogkelolashop

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentDialogKelolaShopBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.profile.ProfileFragment
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopOneFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class DialogKelolaShopFragment : BottomSheetDialogFragment(), DialogKelolaShopClickCallback {

    private lateinit var binding: FragmentDialogKelolaShopBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var shopsDataProfile: Shops

    private val dialogKelolaShopAdapter = DialogKelolaShopAdapter(this@DialogKelolaShopFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogKelolaShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    setDataRvListOrders(shopsDataProfile)
                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })
        binding.btnTambahToko.setOnClickListener {
            addShop()
        }

    }

    private fun addShop() {
        val mCreateShopOneFragment = CreateShopOneFragment()
        setCurrentFragment(mCreateShopOneFragment)
        dismiss()
    }

    private fun setDataRvListOrders(shopsDataProfile: Shops) {
        mainViewModel.getListShops(shopsDataProfile.mitraId.toString())
            .observe(viewLifecycleOwner, { listShops ->
                if (listShops != null && listShops.isNotEmpty()) {
                    dialogKelolaShopAdapter.setListShops(listShops)
                    setShopsAdapter()
                }
            })
    }

    private fun setShopsAdapter() {
        with(binding.rvListToko) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = dialogKelolaShopAdapter
        }
    }

    override fun onItemClicked(data: Shops) {
        val preference = requireContext().getSharedPreferences(
            DialogKelolaShopViewHolder.PREFS_NAME,
            Context.MODE_PRIVATE
        )
        val editor = preference.edit()
        editor.putString("shopId", data.shopId)
        editor.apply()

        mainViewModel.setShopsProfile(data.shopId.toString()).observe(this, { shopsProfile ->
            if (shopsProfile != null) {
                shopsDataProfile = shopsProfile
                val mProfileFragment = ProfileFragment()
                setCurrentFragment(mProfileFragment)
                dismiss()
                Toast.makeText(
                    context,
                    "Berhasil mengubah Toko. Anda sedang berada dalam Toko ${data.shopName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun setCurrentFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.host_fragment_activity_main, fragment)
        }
    }

}