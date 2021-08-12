package com.ibunda.ilifeapps.ui.dashboard.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.databinding.FragmentHomeBinding
import com.ibunda.ilifeapps.ui.dashboard.home.dialogeditprofile.DialogEditProfileFragment
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.dialogbatalkanpesanan.DialogBatalkanPesananFragment
import com.ibunda.ilifeapps.ui.listmitra.ListMitraActivity
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.dialogtawarmitra.DialogTawarMitraFragment

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding


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

        binding.kategoriKebersihan.setOnClickListener {
            val intent = Intent(requireActivity(), ListMitraActivity::class.java)
//            intent.putExtra(MainActivity.EXTRA_USER, user)
            startActivity(intent)
        }

        binding.kategoriLainnya.setOnClickListener {
            showDialogCancelOrder()
//            val mFragmentManager = parentFragmentManager
//            val mCustomOrderFragment = CustomOrderFragment()
//            mFragmentManager.commit {
//                addToBackStack(null)
//                replace(
//                    R.id.host_fragment_activity_main,
//                    mCustomOrderFragment,
//                    MainActivity.CHILD_FRAGMENT
//                )
//            }
        }

    }

    private fun showDialogCancelOrder() {
        val mDialogBatalkanPesananFragment = DialogBatalkanPesananFragment()
        mDialogBatalkanPesananFragment.show(
            requireActivity().supportFragmentManager,
            DialogBatalkanPesananFragment::class.java.simpleName
        )
    }

    private fun showDialogTawar() {
        val mDialogTawarMitraFragment = DialogTawarMitraFragment()
        mDialogTawarMitraFragment.show(
            requireActivity().supportFragmentManager,
            DialogTawarMitraFragment::class.java.simpleName
        )
    }

    private fun showDialogEditProfile() {
        val mFragmentManager = parentFragmentManager
        val mDialogEditProfileFragment = DialogEditProfileFragment()
        mDialogEditProfileFragment.isCancelable = false
        mDialogEditProfileFragment.show(mFragmentManager, "DialogEditProfileFragment")
    }
}