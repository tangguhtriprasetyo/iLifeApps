package com.ibunda.ilifeapps.ui.listmitra.listshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ibunda.ilifeapps.data.firebase.FirebaseServices
import com.ibunda.ilifeapps.data.model.Shops
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ListShopViewModel : ViewModel() {

    private val firebaseServices: FirebaseServices = FirebaseServices()

    private val _filterQuery = MutableLiveData<String>()
    val filterQuery: LiveData<String> = _filterQuery

    fun getListShop(
        categoryName: String?,
        promo: Boolean,
        search: String?
    ): LiveData<List<Shops>?> {
        return firebaseServices.getListShopData(categoryName, "shops", promo, search).asLiveData()
    }

    fun setFilterQuery(query: String) {
        this._filterQuery.value = query
    }

}