package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.dialogorder

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentDialogOrderBinding


class DialogOrderFragment : DialogFragment() {

    private var mView: View? = null
    private var _binding: FragmentDialogOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            _binding = FragmentDialogOrderBinding.inflate(layoutInflater).apply {

                btnPilihJadwal.setOnClickListener {
                    val checkedRadioButtonId = binding.rgKategori.checkedRadioButtonId
                    if (checkedRadioButtonId != 1) {
                        var jadwal: String? = null
                        when (checkedRadioButtonId) {
                            R.id.rb_atur_jadwal -> jadwal =
                                binding.rbAturJadwal.text.toString().trim()
                            R.id.rb_pesan_sekarang -> jadwal = binding.rbPesanSekarang.text.toString().trim()
                        }
                        order(jadwal)
                        dialog?.dismiss()
                    }
                }

            }
            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun order(jadwal: String?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mView = null
    }
}