package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.dialogbatalkanpesanan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentDialogBatalkanPesananBinding

class DialogBatalkanPesananFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogBatalkanPesananBinding

    private var kategori: String? = null

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


        binding.btnBatalkanPesanan.isEnabled = true
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
            }

            cancelOrder(kategori)
            Log.d(kategori, "result")
            dialog?.dismiss()
        }


    }

    private fun cancelOrder(kategori: String?) {
        Toast.makeText(requireContext(), kategori, Toast.LENGTH_SHORT).show()
    }

}