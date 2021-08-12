package com.ibunda.ilifeapps.ui.listmitra.listshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.vo.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ListShopViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    fun getListShop(categoryName: String): LiveData<Resource<PagedList<Shops>?>> {
        return firebaseServices.getListShop(categoryName).asLiveData()
    }

}