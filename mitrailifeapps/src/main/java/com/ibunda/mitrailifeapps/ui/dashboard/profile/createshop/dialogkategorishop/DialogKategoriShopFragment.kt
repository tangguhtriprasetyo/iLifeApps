package com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.dialogkategorishop

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.databinding.FragmentDialogKategoriShopBinding
import com.ibunda.mitrailifeapps.ui.dashboard.profile.createshop.CreateShopTwoFragment


class DialogKategoriShopFragment : DialogFragment() {

    private var mView: View? = null
    private var _binding: FragmentDialogKategoriShopBinding? = null
    private val binding get() = _binding!!
    private var optionDialogListener: OnOptionDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            _binding = FragmentDialogKategoriShopBinding.inflate(layoutInflater).apply {

                btnPilihKategori.setOnClickListener {
                    val checkedRadioButtonId = binding.rgKategori.checkedRadioButtonId
                    if (checkedRadioButtonId != 1) {
                        var kategori: String? = null
                        when (checkedRadioButtonId) {
                            R.id.servis_elektronik -> kategori = binding.servisElektronik.text.toString().trim()
                            R.id.kebersihan -> kategori = binding.kebersihan.text.toString().trim()
                            R.id.asisten_kesehatan -> kategori = binding.asistenKesehatan.text.toString().trim()
                            R.id.tukang -> kategori = binding.tukang.text.toString().trim()
                            R.id.guru_les -> kategori = binding.guruLes.text.toString().trim()
                            R.id.lainnya -> kategori = binding.lainnya.text.toString().trim()
                        }
                        optionDialogListener?.onOptionChosen(kategori)
                        dialog?.dismiss()
                    }
                }
            }
            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment

        if (fragment is CreateShopTwoFragment) {
            val mCreateShopTwoFragment = fragment
            this.optionDialogListener = mCreateShopTwoFragment.optionDialogListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.optionDialogListener = null
    }

    interface OnOptionDialogListener {
        fun onOptionChosen(category: String?)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mView = null
    }

}