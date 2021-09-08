package com.ibunda.ilifeapps.ui.dashboard.home.customorder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.slider.RangeSlider
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentCustomOrderBinding
import com.ibunda.ilifeapps.ui.dashboard.MainViewModel
import com.ibunda.ilifeapps.ui.dashboard.home.customorder.dialogkategorimitra.DialogKategoriMitraFragment
import com.ibunda.ilifeapps.ui.maps.MapsActivity
import com.ibunda.ilifeapps.utils.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt


class CustomOrderFragment : Fragment() {

    private lateinit var binding : FragmentCustomOrderBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var userData: Users
    private lateinit var orders: Orders

    lateinit var datePicker: DatePickerHelper
    lateinit var timePicker: TimePickerHelper

    var isLainnya: Boolean? = null
    var penyediaJasa: String? = null

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

        userData = Users()
        //dataUser
        mainViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userData = userProfile
                    initView()
                }
                Log.d("ViewModelProfileData: ", userProfile.toString())
            })


        datePicker = DatePickerHelper(requireContext())
        timePicker = TimePickerHelper(requireContext(), true)
        initView()

    }

    private fun initView() {

        binding.etLokasi.setText(userData.address)
        binding.etLokasi.setOnClickListener {
            val intent =
                Intent(requireActivity(), MapsActivity::class.java)
            intent.putExtra(MapsActivity.EXTRA_USER_MAPS, userData)
            startActivity(intent)
        }

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.etTanggalPesanan.setOnClickListener {
            datePicker()
        }

        binding.etWaktuDari.setOnClickListener {
            timePicker("from")
        }

        binding.etWaktuSampai.setOnClickListener {
            timePicker("to")
        }

        binding.rgPenyediaJasa.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_pria -> penyediaJasa = binding.rbPria.text.toString()
                R.id.rb_wanita -> penyediaJasa = binding.rbWanita.text.toString()
                R.id.rb_tidak_ada -> penyediaJasa = binding.rbTidakAda.text.toString()
            }
        })

        binding.etKategori.setOnClickListener {
            val mDialogKategoriMitraFragment = DialogKategoriMitraFragment()
            val mFragmentManager = childFragmentManager
            mDialogKategoriMitraFragment.show(
                mFragmentManager,
                DialogKategoriMitraFragment::class.java.simpleName
            )
        }

        binding.rangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
            val values = (binding.rangeSlider.values)
            binding.tvValueMin.text = PriceFormatHelper.getPriceFormat(values[0].roundToInt())
            binding.tvValueMax.text = PriceFormatHelper.getPriceFormat(values[1].roundToInt())
        })

        binding.btnBuatPesanan.setOnClickListener {
            uploadOrderKhusus()
        }
    }

    private fun uploadOrderKhusus() {
        setLoading(true)
        setDataOrder()
        order(orders)
    }

    private fun order(orders: Orders) {
        mainViewModel.uploadOrder(orders).observe(viewLifecycleOwner, { status ->
            if (status == AppConstants.STATUS_SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    "Pesanan berhasil diproses, silahkan lihat status pesanan anda di halaman Transaksi",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().supportFragmentManager.popBackStackImmediate()
            } else {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        })
        setLoading(false)
    }

    private fun setDataOrder() {
        orders = Orders(
            orderId = "KHUSUS" + userData.userId,
            address = userData.address,
            categoryName = binding.etKategori.text.toString(),
            createdAt = DateHelper.getCurrentDateTime(),
            orderDate = binding.etTanggalPesanan.text.toString(),
            orderKhusus = true,
            orderLainnya = isLainnya,
            orderTime = binding.etWaktuDari.text.toString() + " - " + binding.etWaktuSampai.text.toString(),
            payment = binding.etPayment.text.toString(),
            status = "Pesanan",
            totalPrice = binding.tvValueMin.text.toString() + " - " + binding.tvValueMax.text.toString(),
            userId = userData.userId,
            userName = userData.name,
            userPicture = userData.avatar,
            latitude = userData.latitude,
            longitude = userData.longitude,
            jobName = binding.etNamaPekerjaan.text.toString(),
            jobDesc = binding.etDeskripsiPekerjaan.text.toString(),
            jobPerson = penyediaJasa
        )
    }

    private fun datePicker() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.setMinDate(cal.timeInMillis)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(datePicker: View, dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                val date = "${dayStr}/${monthStr}/${year}"
                binding.etTanggalPesanan.setText(date)
            }
        })
    }

    private fun timePicker(value: String) {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
                if (value == "from") {
                    binding.etWaktuDari.setText("${hourStr}:${minuteStr}")
                } else {
                    binding.etWaktuSampai.setText("${hourStr}:${minuteStr}")
                }
            }
        })
    }

    internal var optionDialogListener: DialogKategoriMitraFragment.OnOptionDialogListener = object : DialogKategoriMitraFragment.OnOptionDialogListener {
        override fun onOptionChosen(category: String?) {
            val kategoriMitra: String? = category
            binding.etKategori.setText(kategoriMitra)
            isLainnya = false
            if (category.equals("Lainnya")) {
                isLainnya = true
            }
            Log.e(isLainnya.toString(), "isLainnya")
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnBuatPesanan.isClickable = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnBuatPesanan.isClickable = true
        }
    }

}