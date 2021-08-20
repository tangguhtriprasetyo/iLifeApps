package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.dialogbatalkanpesanan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentDialogBatalkanPesananBinding
import com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.PesananFragment

class DialogBatalkanPesananFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogBatalkanPesananBinding

    private var kategori: String? = null
    private var optionDialogListener: OnOptionDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogBatalkanPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icClose.setOnClickListener {
            onDismiss(dialog!!)
        }

        binding.btnBatalkanPesanan.setOnClickListener {

            val checkedRadioButtonId = binding.rgKategori.checkedRadioButtonId
            if (checkedRadioButtonId != 1) {
                when (checkedRadioButtonId) {
                    R.id.rb_ubah_alamat -> kategori = binding.rbUbahAlamat.text.toString().trim()
                    R.id.rb_ubah_pesanan -> kategori = binding.rbUbahPesanan.text.toString().trim()
                    R.id.rb_nemu_mitra -> kategori = binding.rbNemuMitra.text.toString().trim()
                    R.id.rb_tidak_pesan -> kategori = binding.rbTidakPesan.text.toString().trim()
                    R.id.rb_lainnya -> kategori = binding.rbLainnya.text.toString().trim()
                }
                optionDialogListener?.onOptionChosen(kategori)
                dialog?.dismiss()
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment

        if (fragment is PesananFragment) {
            val pesananFragment = fragment
            this.optionDialogListener = pesananFragment.optionDialogListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.optionDialogListener = null
    }

    interface OnOptionDialogListener {
        fun onOptionChosen(category: String?)
    }

}