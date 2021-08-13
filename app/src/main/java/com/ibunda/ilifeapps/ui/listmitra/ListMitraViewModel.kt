package com.ibunda.ilifeapps.ui.listmitra

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListMitraViewModel : ViewModel()
{
    val dataCategory = MutableLiveData<String>()

    fun dataCategory(categoryName: String) {
        dataCategory.value = categoryName
    }
}