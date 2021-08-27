package com.ibunda.mitrailifeapps.ui.detailorder.pesanan.dialogtolakpesanan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.databinding.FragmentDialogTolakPesananBinding
import com.ibunda.mitrailifeapps.ui.detailorder.pesanan.PesananFragment


class DialogTolakPesananFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentDialogTolakPesananBinding

    private var kategori: String? = null
    private var optionDialogListener: DialogTolakPesananFragment.OnOptionDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDialogTolakPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icClose.setOnClickListener {
            onDismiss(dialog!!)
        }

        binding.btnTolakPesanan.setOnClickListener {

            val checkedRadioButtonId = binding.rgKategori.checkedRadioButtonId
            if (checkedRadioButtonId != 1) {
                when (checkedRadioButtonId) {
                    R.id.rb_pesanan_penuh -> kategori = binding.rbPesananPenuh.text.toString().trim()
                    R.id.rb_masih_kerja -> kategori = binding.rbMasihKerja.text.toString().trim()
                    R.id.rb_sedang_istirahat -> kategori = binding.rbSedangIstirahat.text.toString().trim()
                    R.id.rb_lainnya -> kategori = binding.etRbLainnya.text.toString().trim()
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