package com.ibunda.mitrailifeapps.ui.detailorder.pesanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibunda.mitrailifeapps.databinding.FragmentPesananBinding
import com.ibunda.mitrailifeapps.ui.detailorder.pesanan.dialogtolakpesanan.DialogTolakPesananFragment


class PesananFragment : Fragment() {

    private lateinit var binding : FragmentPesananBinding

    private var reasonCancel: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    internal var optionDialogListener: DialogTolakPesananFragment.OnOptionDialogListener = object : DialogTolakPesananFragment.OnOptionDialogListener {
        override fun onOptionChosen(reason: String?) {
            reasonCancel = reason
        }
    }

}