package com.ibunda.mitrailifeapps.ui.dashboard.home.detailpesanankhusus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.databinding.FragmentDetailPesananKhususBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.ui.dashboard.home.detailpesanankhusus.dialogtawarjasa.DialogTawarJasaFragment
import com.ibunda.mitrailifeapps.ui.dashboard.home.detailpesanankhusus.dialogtawarjasa.DialogTawarJasaFragment.Companion.EXTRA_ORDER_DIALOG_DATA
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class DetailPesananKhususFragment : Fragment() {

    private lateinit var binding : FragmentDetailPesananKhususBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var ordersData: Orders

    companion object {
        const val EXTRA_ORDER_DATA = "extra_order_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailPesananKhususBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersData = Orders()
        if (arguments != null) {
            ordersData = requireArguments().getParcelable(EXTRA_ORDER_DATA)!!
        }
        Log.d(ordersData.orderId.toString(), "orderId")

        mainViewModel.setOrderData(ordersData.orderId.toString())
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    ordersData = orders
                }
            })

        initView()
        getOrderData()


    }

    private fun getOrderData() {
        mainViewModel.getOrderData()
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    ordersData = orders
                    setOrderData(ordersData)
                }
                Log.d("ViewModelShopData: ", orders.toString())
            })
    }

    private fun setOrderData(ordersData: Orders) {
        with(binding) {
            tvNamaMitra.text = ordersData.userName
            tvTotalDanaPesananKhusus.text = ordersData.totalPrice
            imgProfile.loadImage(ordersData.userPicture)
            tvKategoriMitra.text = ordersData.categoryName
            tvCreatedAt.text = ordersData.createdAt
            tvValueNamaPekerjaan.text = ordersData.jobName
            tvValueDeskripsiPekerjaan.text = ordersData.jobDesc
            tvValuePenyediaJasa.text = ordersData.jobPerson
            tvDate.text = ordersData.orderDate
            tvTimeOrder.text = ordersData.orderTime
            tvLokasi.text = ordersData.address
            tvMetodePembayaran.text = ordersData.payment
        }
    }

    private fun initView() {

        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE
        binding.icBack.setOnClickListener {
            bottomNav.visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.btnTawarkanJasa.setOnClickListener {
            dialogTawar()
        }

    }

    private fun dialogTawar() {
        val mDialogTawarJasaFragment = DialogTawarJasaFragment()
        val mBundle = Bundle()
        mBundle.putParcelable(EXTRA_ORDER_DIALOG_DATA, ordersData)
        mDialogTawarJasaFragment.arguments = mBundle
        val mFragmentManager = childFragmentManager
        mDialogTawarJasaFragment.show(mFragmentManager, DialogTawarJasaFragment::class.java.simpleName)
    }

}