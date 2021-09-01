package com.ibunda.mitrailifeapps.ui.dashboard.home.detailpesanankhusus.dialogtawarjasa

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.mitrailifeapps.databinding.FragmentDialogTawarJasaBinding
import com.ibunda.mitrailifeapps.utils.PriceFormatHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class DialogTawarJasaFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogTawarJasaBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogTawarJasaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icClose.setOnClickListener {
            onDismiss(dialog!!)
        }

        binding.etTawar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                binding.btnTawar.isEnabled = true
            }
        })

        binding.btnTawar.setOnClickListener {
            val tawar: Int = binding.etTawar.text.toString().toInt()
            val tawarPrice = PriceFormatHelper.getPriceFormat(tawar)
            Toast.makeText(requireContext(), tawarPrice, Toast.LENGTH_SHORT).show()
        }

    }

}