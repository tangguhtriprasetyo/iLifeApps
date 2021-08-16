package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentPaymentBinding
import com.ibunda.ilifeapps.utils.DatePickerHelper
import com.ibunda.ilifeapps.utils.TimePickerHelper
import java.util.*




class PaymentFragment : Fragment() {

    private lateinit var binding : FragmentPaymentBinding

    lateinit var datePicker: DatePickerHelper
    lateinit var timePicker: TimePickerHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker = DatePickerHelper(requireContext())
        timePicker = TimePickerHelper(requireContext(), true)
        initView()

    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rb_pesan_sekarang ->
                    if (checked) {
                        binding.linearWaktuPesanan.isEnabled = false
                        binding.linearTanggalPesanan.isEnabled = false
                        binding.icNext.visibility = View.GONE
                        binding.icNext2.visibility = View.GONE
                        binding.tvTanggalPesanan.setTextColor(ContextCompat.getColor(requireContext(), com.ibunda.ilifeapps.R.color.fontulasan))
                        binding.tvWaktuPesanan.setTextColor(ContextCompat.getColor(requireContext(), com.ibunda.ilifeapps.R.color.fontulasan))
                    }
                R.id.rb_atur_jadwal ->
                    if (checked) {
                        binding.linearTanggalPesanan.setOnClickListener {
                            timePicker()
                        }
                        binding.linearWaktuPesanan.setOnClickListener {
                            datePicker()
                        }
                    }
            }
        }
    }

    private fun datePicker() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.setMaxDate(cal.timeInMillis)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(datePicker: View, dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                val date = "${dayStr}/${monthStr}/${year}"
                binding.tvWaktuPesanan.setText(date)
            }
        })
    }

    private fun timePicker() {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
                binding.tvTanggalPesanan.text = "${hourOfDay}:${minuteStr}"
            }
        })
    }
}