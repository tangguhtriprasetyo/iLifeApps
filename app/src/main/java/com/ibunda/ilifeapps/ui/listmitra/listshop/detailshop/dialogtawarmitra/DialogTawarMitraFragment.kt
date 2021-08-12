package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.dialogtawarmitra

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.ilifeapps.databinding.FragmentDialogTawarMitraBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DialogTawarMitraFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogTawarMitraBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogTawarMitraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icClose.setOnClickListener {
            onDismiss(dialog!!)
        }

        binding.etTawar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                binding.btnTawar.isEnabled = true
            }
        })

        binding.btnTawar.setOnClickListener {
            val tawarPrice: Int = binding.etTawar.text.toString().toInt()
            Toast.makeText(requireContext(), tawarPrice.toString(), Toast.LENGTH_SHORT).show()
        }

    }


}