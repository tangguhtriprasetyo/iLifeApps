package com.ibunda.ilifeapps.ui.listmitra

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.databinding.ActivityListMitraBinding
import com.ibunda.ilifeapps.ui.listmitra.listshop.ListShopFragment

class ListMitraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMitraBinding

    private val listMitraViewModel: ListMitraViewModel by viewModels()

    companion object {
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var categoryName: String? = null
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME)
        Log.d(categoryName, "categoryName")
        
        listMitraViewModel.dataCategory.value = categoryName

        val listShopFragment = ListShopFragment()
        supportFragmentManager.commit {
            replace(R.id.host_listshop_activity, listShopFragment)
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}