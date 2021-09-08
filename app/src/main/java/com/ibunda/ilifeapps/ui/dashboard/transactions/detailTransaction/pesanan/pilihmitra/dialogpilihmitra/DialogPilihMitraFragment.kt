package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra.dialogpilihmitra

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ibunda.ilifeapps.databinding.FragmentDialogPilihMitraBinding

class DialogPilihMitraFragment : DialogFragment() {

    private var mView: View? = null
    private var _binding: FragmentDialogPilihMitraBinding? = null
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            _binding = FragmentDialogPilihMitraBinding.inflate(layoutInflater).apply {

                btnTidak.setOnClickListener {
                    dialog?.dismiss()
                }

                binding.btnPilih.setOnClickListener {

                }

            }
            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mView = null
    }


}