package com.ibunda.ilifeapps.ui.listmitra.listshop.dialogseleksiberdasarkan

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.databinding.FragmentDialogSeleksiBerdasarkanBinding
import com.ibunda.ilifeapps.ui.listmitra.listshop.ListShopViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DialogSeleksiBerdasarkanFragment : DialogFragment() {

    private var mView: View? = null
    private var _binding: FragmentDialogSeleksiBerdasarkanBinding? = null
    private val binding get() = _binding!!

    private val listShopViewModel: ListShopViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            //initiate the binding here and pass the root to the dialog view
            _binding = FragmentDialogSeleksiBerdasarkanBinding.inflate(layoutInflater).apply {

                icClose.setOnClickListener {
                    dialog?.dismiss()
                }

                rbPopularitas.setOnCheckedChangeListener { _, _ ->
                    val sort = "Popularitas"
                    listShopViewModel.setFilterQuery(sort)
                    dialog?.dismiss()
                }

                rbJarak.setOnCheckedChangeListener { _, _ ->
                    val sort = "Distance"
                    listShopViewModel.setFilterQuery(sort)
                    dialog?.dismiss()
                }

                rbRating.setOnCheckedChangeListener { _, _ ->
                    val sort = "Rating"
                    listShopViewModel.setFilterQuery(sort)
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