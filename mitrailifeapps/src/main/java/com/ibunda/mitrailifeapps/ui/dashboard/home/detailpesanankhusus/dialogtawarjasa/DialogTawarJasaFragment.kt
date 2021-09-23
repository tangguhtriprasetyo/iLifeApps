package com.ibunda.mitrailifeapps.ui.dashboard.home.detailpesanankhusus.dialogtawarjasa

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ibunda.mitrailifeapps.R
import com.ibunda.mitrailifeapps.data.model.Notifications
import com.ibunda.mitrailifeapps.data.model.OfferOrder
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.databinding.FragmentDialogTawarJasaBinding
import com.ibunda.mitrailifeapps.ui.dashboard.MainViewModel
import com.ibunda.mitrailifeapps.utils.AppConstants
import com.ibunda.mitrailifeapps.utils.DateHelper
import com.ibunda.mitrailifeapps.utils.PriceFormatHelper
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class DialogTawarJasaFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogTawarJasaBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var ordersData: Orders
    private lateinit var offerOrder: OfferOrder
    private lateinit var shopsDataProfile: Shops

    private lateinit var progressDialog : Dialog

    companion object {
        const val EXTRA_ORDER_DIALOG_DATA = "extra_order_dialog_data"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogTawarJasaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersData = Orders()
        progressDialog = ProgressDialogHelper.progressDialog(requireContext())

        if (arguments != null) {
            ordersData = requireArguments().getParcelable(EXTRA_ORDER_DIALOG_DATA)!!
        }

        binding.icClose.setOnClickListener {
            onDismiss(dialog!!)
        }

        binding.etTawar.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                binding.btnTawar.isEnabled = true
            }
        })

        binding.btnTawar.setOnClickListener {
            progressDialog.show()
            mainViewModel.getShopData()
                .observe(viewLifecycleOwner, { shopsProfile ->
                    if (shopsProfile != null) {
                        shopsDataProfile = shopsProfile
                        tawarJasa(shopsDataProfile)
                    }
                    Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
                })
        }
    }

    private fun tawarJasa(shopsDataProfile: Shops) {
        val tawar: Int = binding.etTawar.text.toString().toInt()
        val tawarPrice = PriceFormatHelper.getPriceFormat(tawar)
        val tawarId =  shopsDataProfile.shopId + ordersData.orderId
        offerOrder = OfferOrder(
            tawarId = tawarId,
            shopId = shopsDataProfile.shopId,
            shopName = shopsDataProfile.shopName,
            shopPicture = shopsDataProfile.shopPicture,
            verified = shopsDataProfile.verified,
            priceTawar = tawarPrice,
            orderId = ordersData.orderId,
            userId = ordersData.userId,
            rating = shopsDataProfile.rating
        )
        mainViewModel.uploadTawaran(ordersData.orderId.toString(), tawarId, offerOrder).observe(viewLifecycleOwner, { status ->
            if (status == AppConstants.STATUS_SUCCESS) {
                sendNotif(shopsDataProfile)
                progressDialog.dismiss()
                onDismiss(dialog!!)
                val bottomNav: BottomNavigationView =
                    requireActivity().findViewById(R.id.bottom_navigation)
                bottomNav.visibility = View.VISIBLE
                requireActivity().supportFragmentManager.popBackStackImmediate()
            } else {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun sendNotif(shopsDataProfile: Shops) {
        val notif = Notifications(
            date = DateHelper.getCurrentDateTime(),
            body = AppConstants.TITLE_ORDER_TAWAR,
            title = AppConstants.BODY_ORDER_TAWAR,
            orderId = ordersData.orderId,
            receiverId = ordersData.userId,
            receiverPicture = ordersData.userPicture,
            senderId = shopsDataProfile.shopId,
            senderPicture = shopsDataProfile.shopPicture
        )
        mainViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    Toast.makeText(
                        requireContext(),
                        AppConstants.TOAST_ORDER_TAWAR,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })

    }


}