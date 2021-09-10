package com.ibunda.ilifeapps.ui.dashboard.transactions.detailTransaction.pesanan.pilihmitra

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibunda.ilifeapps.databinding.FragmentPilihMitraBinding

class PilihMitraFragment : Fragment() {
    private lateinit var binding: FragmentPilihMitraBinding

    private val pilihMitraViewModel: PilihMitraViewModel by activityViewModels()
    private val pilihMitraAdapter = PilihMitraAdapter()

    private var orderId: String? = null

    companion object {
        const val EXTRA_ORDER_ID = "extra_order_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPilihMitraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getListTawaranMitra()
        initView()
    }

    private fun getListTawaranMitra() {
        if (arguments != null) {
            orderId = requireArguments().getString(EXTRA_ORDER_ID)
            orderId?.let {
                pilihMitraViewModel.getListTawaranMitra(it)
                    .observe(viewLifecycleOwner, { listMitra ->
                        if (listMitra != null && listMitra.isNotEmpty()) {
                            pilihMitraAdapter.setListMitra(listMitra)
                            setListMitraAdapter()
                            showEmptyListShop(false)
                        } else {
                            showEmptyListShop(true)
                        }
                    })
            }
        } else {
            showEmptyListShop(true)
        }
    }

    private fun setListMitraAdapter() {
        with(binding.rvPilihMitra) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = pilihMitraAdapter
            Log.d(ContentValues.TAG, "setAdapter: ")
        }
    }

    private fun initView() {
        binding.icBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }
    }


    private fun showEmptyListShop(state: Boolean) {
        if (state) {
            binding.rvPilihMitra.visibility = View.GONE
            binding.linearEmptyPilihMitra.visibility = View.VISIBLE
        } else {
            binding.rvPilihMitra.visibility = View.VISIBLE
            binding.linearEmptyPilihMitra.visibility = View.GONE
        }
    }
}