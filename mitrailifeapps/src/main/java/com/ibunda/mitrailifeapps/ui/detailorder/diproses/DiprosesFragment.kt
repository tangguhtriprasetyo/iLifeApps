package com.ibunda.mitrailifeapps.ui.detailorder.diproses

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ibunda.mitrailifeapps.data.model.Notifications
import com.ibunda.mitrailifeapps.data.model.Orders
import com.ibunda.mitrailifeapps.data.model.Shops
import com.ibunda.mitrailifeapps.data.model.Users
import com.ibunda.mitrailifeapps.databinding.FragmentDiprosesBinding
import com.ibunda.mitrailifeapps.databinding.ItemDialogFinishOrderBinding
import com.ibunda.mitrailifeapps.ui.detailorder.DetailViewModel
import com.ibunda.mitrailifeapps.ui.maps.MapsActivity
import com.ibunda.mitrailifeapps.utils.AppConstants
import com.ibunda.mitrailifeapps.utils.DateHelper
import com.ibunda.mitrailifeapps.utils.ProgressDialogHelper
import com.ibunda.mitrailifeapps.utils.loadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DiprosesFragment : Fragment() {

    private lateinit var binding : FragmentDiprosesBinding

    private val detailViewModel: DetailViewModel by activityViewModels()
    private lateinit var ordersData: Orders
    private lateinit var shopsDataProfile: Shops
    private lateinit var userDataProfile: Users

    private lateinit var progressDialog : Dialog

    companion object {
        const val MULAI_PERJALANAN = "Mulai Perjalanan"
        const val SAMPAI_TUJUAN = "Sampai Tujuan"
        const val PESANAN_SELESAI = "Pesanan Selesai"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiprosesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialogHelper.progressDialog(requireContext())

        getOrdersData()
        initOnClick()
    }

    private fun getOrdersData() {
        detailViewModel.getOrderData()
            .observe(viewLifecycleOwner, { orders ->
                if (orders != null) {
                    ordersData = orders
                    initViewOrder(ordersData)
                }
                Log.d("ViewModelOrder: ", orders.toString())
            })
    }

    private fun initViewOrder(ordersData: Orders) {
        with(binding) {
            tvTotalHargaJasa.text = ordersData.totalPrice
            imgProfileUser.loadImage(ordersData.userPicture)
            tvNamaUser.text = ordersData.userName
            tvCreatedAt.text = ordersData.createdAt
            tvProcessedAt.text = ordersData.processedAt
            btnStatusPekerjaan.text = MULAI_PERJALANAN
            if (ordersData.startAt!= null) {
                tvInfoMulaiPerjalanan.visibility = View.VISIBLE
                tvStartAt.visibility = View.VISIBLE
                tvStartAt.text = ordersData.startAt
                btnStatusPekerjaan.text = SAMPAI_TUJUAN
            }
            if (ordersData.arrivedAt!= null) {
                tvInfoSampaiTujuan.visibility = View.VISIBLE
                tvArrivedAt.visibility = View.VISIBLE
                tvArrivedAt.text = ordersData.arrivedAt
                btnStatusPekerjaan.text = PESANAN_SELESAI
            }
            tvDate.text = ordersData.orderDate
            tvTimeOrder.text = ordersData.orderTime
            tvLokasi.text = ordersData.address
            tvMetodePembayaran.text = ordersData.payment

            val btnStatus = btnStatusPekerjaan.getText().toString()
            Log.e(btnStatus, "statusButton")
            if (btnStatus == MULAI_PERJALANAN) {
                btnStatusPekerjaan.setOnClickListener { mulaiPerjalanan() }
            } else if (btnStatus == SAMPAI_TUJUAN) {
                btnStatusPekerjaan.setOnClickListener{ sampaiTujuan() }
            } else if (btnStatus == PESANAN_SELESAI) {
                btnStatusPekerjaan.setOnClickListener{ dialogFinishOrder() }
            }
        }
    }

    private fun initOnClick() {
        binding.icBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnLihatLokasi.setOnClickListener {
            openMaps()
        }
    }

    private fun openMaps() {
        //getUserData
        detailViewModel.getUserProfileData()
            .observe(viewLifecycleOwner, { userProfile ->
                if (userProfile != null) {
                    userDataProfile = userProfile
                    //openMaps
                    val intent =
                        Intent(requireActivity(), MapsActivity::class.java)
                    intent.putExtra(MapsActivity.EXTRA_USER_MAPS, userDataProfile)
                    startActivity(intent)
                }
                Log.d("ViewModelProfile: ", userProfile.toString())
            })
    }

    private fun dialogFinishOrder() {
        val builder = AlertDialog.Builder(requireContext())
        val binding : ItemDialogFinishOrderBinding = ItemDialogFinishOrderBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.show()
        binding.btnTidak.setOnClickListener { dialog.dismiss() }
        binding.btnYa.setOnClickListener { finishOrder() }
    }

    private fun finishOrder() {
        progressDialog.show()
        ordersData.finishedAt = DateHelper.getCurrentDateTime()
        ordersData.status = AppConstants.STATUS_SELESAI

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                updateTotalPesananShop()
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Update Order Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateTotalPesananShop() {

        detailViewModel.getShopData()
            .observe(viewLifecycleOwner, { shopsProfile ->
                if (shopsProfile != null) {
                    shopsDataProfile = shopsProfile
                    var totalOrder = shopsDataProfile.totalPesananSukses!!
                    Log.e(totalOrder.toString(), "totalOrder")
                    totalOrder+=1
                    shopsDataProfile.totalPesananSukses = totalOrder

                    detailViewModel.updateTotalOrderShop(shopsDataProfile).observe(viewLifecycleOwner, { updateOrder ->
                        if (updateOrder != null) {
                            sendNotif(PESANAN_SELESAI)
                            progressDialog.dismiss()
                            activity?.onBackPressed()
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                requireActivity(),
                                "Update Order Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

                }
                Log.d("ViewModelShopsProfile: ", shopsProfile.toString())
            })
    }

    private fun sampaiTujuan() {
        progressDialog.show()
        ordersData.arrivedAt = DateHelper.getCurrentDateTime()

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                sendNotif(SAMPAI_TUJUAN)
                progressDialog.dismiss()
                activity?.onBackPressed()
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Update Order Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun mulaiPerjalanan() {
        progressDialog.show()
        ordersData.startAt = DateHelper.getCurrentDateTime()

        detailViewModel.updateOrderData(ordersData).observe(viewLifecycleOwner, { updateOrder ->
            if (updateOrder != null) {
                sendNotif(MULAI_PERJALANAN)
                progressDialog.dismiss()
                activity?.onBackPressed()
            } else {
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Update Order Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun sendNotif(result: String) {

        val notif = Notifications(
            date = DateHelper.getCurrentDateTime(),
            orderId = ordersData.orderId,
            receiverId = ordersData.userId,
            receiverPicture = ordersData.userPicture,
            senderId = ordersData.shopId,
            senderPicture = ordersData.shopPicture
        )

        if (result == MULAI_PERJALANAN) {
            notif.body = AppConstants.BODY_STATUS_DIPROSES_MULAI
            notif.title = AppConstants.TITLE_STATUS_DIPROSES_MULAI
            detailViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    Toast.makeText(
                        requireContext(),
                        AppConstants.TOAST_STATUS_DIPROSES,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
        } else if (result == SAMPAI_TUJUAN) {
            notif.body = AppConstants.BODY_STATUS_DIPROSES_SAMPAI
            notif.title = AppConstants.TITLE_STATUS_DIPROSES_SAMPAI
            detailViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    Toast.makeText(
                        requireContext(),
                        AppConstants.TOAST_STATUS_DIPROSES,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            notif.body = AppConstants.BODY_STATUS_DIPROSES_SELESAI
            notif.title = AppConstants.TITLE_STATUS_DIPROSES_SELESAI
            detailViewModel.uploadNotif(notif).observe(viewLifecycleOwner, { status ->
                if (status == AppConstants.STATUS_SUCCESS) {
                    Toast.makeText(
                        requireContext(),
                        AppConstants.TOAST_STATUS_DIPROSES,
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}