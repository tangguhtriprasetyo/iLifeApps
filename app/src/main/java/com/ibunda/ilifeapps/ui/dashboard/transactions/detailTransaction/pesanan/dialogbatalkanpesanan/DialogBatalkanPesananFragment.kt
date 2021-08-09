package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.dialogbatalkanpesanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.ilifeapps.databinding.FragmentDialogBatalkanPesananBinding

class DialogBatalkanPesananFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogBatalkanPesananBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogBatalkanPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}