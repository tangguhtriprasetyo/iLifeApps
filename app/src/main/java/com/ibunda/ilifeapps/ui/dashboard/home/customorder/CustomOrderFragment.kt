package com.ibunda.ilifeapps.ui.dashboard.home.customorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.slider.RangeSlider
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.FragmentCustomOrderBinding
import com.ibunda.ilifeapps.ui.dashboard.home.customorder.dialogkategorimitra.DialogKategoriMitraFragment
import com.ibunda.ilifeapps.utils.PriceFormatHelper
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


class CustomOrderFragment : Fragment() {

    private lateinit var binding : FragmentCustomOrderBinding
    var kategoriMitra: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomOrderBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        initView()

        binding.etKategori.setOnClickListener {
            val mDialogKategoriMitraFragment = DialogKategoriMitraFragment()
            val mFragmentManager = childFragmentManager
            mDialogKategoriMitraFragment.show(mFragmentManager, DialogKategoriMitraFragment::class.java.simpleName)
        }

    }

    private fun initView() {
        binding.rangeSlider.addOnChangeListener(object : RangeSlider.OnChangeListener{
            override fun onValueChange(slider: RangeSlider, value: Float, fromUser: Boolean) {

                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("USD")
                format.format(value.toDouble())

                val values = (binding.rangeSlider.values)

                binding.tvValueMin.text = PriceFormatHelper.getPriceFormat(values[0].roundToInt())
                binding.tvValueMax.text = PriceFormatHelper.getPriceFormat(values[1].roundToInt())
            }
        })
    }

    internal var optionDialogListener: DialogKategoriMitraFragment.OnOptionDialogListener = object : DialogKategoriMitraFragment.OnOptionDialogListener {
        override fun onOptionChosen(category: String?) {
            Toast.makeText(activity, category, Toast.LENGTH_SHORT).show()
            kategoriMitra = category
            binding.etKategori.setText(kategoriMitra)
        }
    }


}