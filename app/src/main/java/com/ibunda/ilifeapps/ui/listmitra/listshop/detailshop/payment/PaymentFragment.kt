package com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.payment

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Notifications
import com.ibunda.ilifeapps.data.model.Orders
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.FragmentPaymentBinding
import com.ibunda.ilifeapps.ui.listmitra.ListMitraViewModel
import com.ibunda.ilifeapps.utils.*
import java.util.*
import kotlin.math.roundToInt

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding

    private val listMitraViewModel: ListMitraViewModel by activityViewModels()
    private lateinit var userData: Users
    private lateinit var shopData: Shops
    private lateinit var orders: Orders

    lateinit var datePicker: DatePickerHelper
    lateinit var timePicker: TimePickerHelper

    companion object {
        const val EXTRA_HARGA_TAWAR = "extra_harga_tawar"
    }

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

        userData = Users()
        shopData = Shops()
        //dataUser
        listMitraViewModel.getProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userData = userProfile
                    Log.e(userData.userId, "userId")
                    Log.e(userData.name, "userName")
                    listMitraViewModel.getShopData()
                        .observe(viewLifecycleOwner, { shops ->
                            if (shops != null) {
                                shopData = shops
                                Log.e(shopData.shopId, "shopId")
                                initView()
                            }
                            Log.d("ViewModelShopData: ", shops.toString())
                        })
                }
                Log.d("ViewModelProfileData: ", userProfile.toString())
            })
        //dataShop

        datePicker = DatePickerHelper(requireContext())
        timePicker = TimePickerHelper(requireContext(), true)

    }

    private fun initView() {
        //schedule
        binding.rbBayarDitempat.isChecked = true
        binding.rbPesanSekarang.isChecked = true
        if (binding.rbPesanSekarang.isChecked) {
            scheduleNow()
        }
        binding.rgScheduleOrcer.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_pesan_sekarang -> scheduleNow()
                R.id.rb_atur_jadwal -> scheduleLater()
            }
        })

        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
        binding.btnOrder.setOnClickListener {
            setDataOrder()
            order(orders)
        }
        //INVOICE
        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        var price: Int? = 0
        val priceTawar = requireArguments().getInt(EXTRA_HARGA_TAWAR, 0)
        Log.d("HARGA TAWAR", "$priceTawar")
        if (shopData.promo) {
            price = shopData.shopPromo
        } else if (priceTawar != 0) {
            price = priceTawar
        } else {
            price = shopData.price
        }
        if (userData.latitude != null || userData.longitude != null) {

            val userLocation = Location("userLocation")
            userLocation.latitude = userData.latitude!!
            userLocation.longitude = userData.longitude!!

            val shopLocation = Location("shopLocation")
            shopLocation.latitude = shopData.latitude!!
            shopLocation.longitude = shopData.longitude!!

            val distance: Int = (userLocation.distanceTo(shopLocation)).roundToInt()
            val priceOngkir = if (distance <= 6000) 6000 else {
                (distance.div(1000)) * 2500
            }
            binding.tvPriceJasa.text = PriceFormatHelper.getPriceFormat(price)
            binding.tvPriceOngkir.text = PriceFormatHelper.getPriceFormat(priceOngkir)
            binding.tvTotalPrice.text =
                PriceFormatHelper.getPriceFormat((price!!.plus(priceOngkir)))
        } else {
            Toast.makeText(requireContext(), "Mohon Tentukan Lokasi Anda!", Toast.LENGTH_SHORT)
                .show()
            parentFragment?.childFragmentManager?.popBackStackImmediate()
        }
    }

    private fun setDataOrder() {
        val orderId = "${userData.userId}${shopData.shopId}-${userData.totalOrder}"
        orders = Orders(
            orderId = orderId,
            address = userData.address,
            categoryName = shopData.categoryName,
            createdAt = DateHelper.getCurrentDateTime(),
            orderDate = binding.tvDate.text.toString(),
            orderKhusus = false,
            orderLainnya = false,
            orderTime = binding.tvTimeOrder.text.toString(),
            payment = binding.rbBayarDitempat.text.toString(),
            shopId = shopData.shopId,
            shopName = shopData.shopName,
            shopPicture = shopData.shopPicture,
            status = "Pesanan",
            totalPrice = binding.tvTotalPrice.text.toString(),
            userId = userData.userId,
            userName = userData.name,
            userPicture = userData.avatar,
            verified = shopData.verified,
            latitude = userData.latitude,
            longitude = userData.longitude
        )
    }

    private fun order(orders: Orders) {
        listMitraViewModel.uploadOrder(orders).observe(viewLifecycleOwner, { status ->
            if (status == AppConstants.STATUS_SUCCESS) {
                sendNotif()
            } else {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendNotif() {
        val notif = Notifications(
            body = AppConstants.MESSAGE_STATUS_PESANAN,
            date = DateHelper.getCurrentDateTime(),
            orderId = orders.orderId,
            receiverId = shopData.shopId,
            receiverPicture = shopData.shopPicture,
            title = AppConstants.TITLE_STATUS_PESANAN,
            senderId = userData.userId,
            senderPicture = userData.avatar
        )
        listMitraViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
            if (status == AppConstants.STATUS_SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    "Pesanan berhasil diproses, silahkan lihat status pesanan anda di halaman Transaksi",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun scheduleLater() {
        binding.tvDate.text = DateHelper.getCurrentDate()
        binding.tvTimeOrder.text = DateHelper.getCurrentTime()
        binding.linearWaktuPesanan.isEnabled = true
        binding.linearTanggalPesanan.isEnabled = true
        binding.icNext.visibility = View.VISIBLE
        binding.icNext2.visibility = View.VISIBLE
        binding.tvTanggalPesanan.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                com.ibunda.ilifeapps.R.color.font_default
            )
        )
        binding.tvWaktuPesanan.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                com.ibunda.ilifeapps.R.color.font_default
            )
        )
        binding.linearTanggalPesanan.setOnClickListener {
            datePicker()
        }
        binding.linearWaktuPesanan.setOnClickListener {
            timePicker()
        }
    }

    private fun scheduleNow() {
        binding.tvDate.text = DateHelper.getCurrentDate()
        binding.tvTimeOrder.text = DateHelper.getCurrentTime()
        binding.linearWaktuPesanan.isEnabled = false
        binding.linearTanggalPesanan.isEnabled = false
        binding.icNext.visibility = View.GONE
        binding.icNext2.visibility = View.GONE
        binding.tvTanggalPesanan.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                com.ibunda.ilifeapps.R.color.fontulasan
            )
        )
        binding.tvWaktuPesanan.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                com.ibunda.ilifeapps.R.color.fontulasan
            )
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
                binding.tvDate.text = date
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
                binding.tvTimeOrder.text = "${hourStr}:${minuteStr}"
            }
        })
    }
}