package com.ibunda.ilifeapps.ui.listmitra.listshop.dialogseleksiberdasarkan

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ibunda.ilifeapps.databinding.FragmentDialogSeleksiBerdasarkanBinding

class DialogSeleksiBerdasarkanFragment : DialogFragment() {

    private var mView: View? = null
    private var _binding: FragmentDialogSeleksiBerdasarkanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            _binding = FragmentDialogSeleksiBerdasarkanBinding.inflate(layoutInflater).apply {

                icClose.setOnClickListener {
                    dialog?.dismiss()
                }
                var sort: String? = null

                rbRekomendasi.setOnCheckedChangeListener { buttonView, isChecked ->
                    sort = "Rekomendasi"
                    Toast.makeText(requireContext(), sort, Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }

                rbJarak.setOnCheckedChangeListener { buttonView, isChecked ->
                    sort = "Jarak"
                    Toast.makeText(requireContext(), sort, Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }

                rbRating.setOnCheckedChangeListener { buttonView, isChecked ->
                    sort = "Rating"
                    Toast.makeText(requireContext(), sort, Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
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