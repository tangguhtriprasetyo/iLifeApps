package com.ibunda.ilifeapps.ui.listmitra

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ActivityListMitraBinding
import com.ibunda.ilifeapps.ui.listmitra.listshop.ListShopFragment
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.DetailShopFragment

class ListMitraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMitraBinding

    private val listMitraViewModel: ListMitraViewModel by viewModels()
    private lateinit var user: Users
    private var shopData = Shops()

    companion object {
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
        const val EXTRA_USER = "extra_user"
        const val EXTRA_SHOP = "extra_shop"
        const val EXTRA_PROMO = "extra_promo"
        const val EXTRA_TRANSACTION = "extra_transaction"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var categoryName: String? = null
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME)
        Log.d(categoryName, "categoryName")
        listMitraViewModel.dataCategory.value = categoryName

        if (intent.hasExtra(EXTRA_USER)) {
            user = intent.getParcelableExtra<Users>(EXTRA_USER) as Users

            listMitraViewModel.setUserProfile(user.userId.toString()).observe(this, { userProfile ->
                if (userProfile != null) {
                    user = userProfile
                }
            })
        }
        if (intent.hasExtra(EXTRA_SHOP)) {
            val shopId = intent.getStringExtra(EXTRA_SHOP)
            var transaction = false
            if (intent.hasExtra(EXTRA_TRANSACTION)) {
                transaction = intent.getBooleanExtra(EXTRA_TRANSACTION, false)
            }
            val profileShopFragment = DetailShopFragment()
            shopData.shopId = shopId
            val mBundle = Bundle()
            mBundle.putParcelable(DetailShopFragment.EXTRA_SHOP_DATA, shopData)
            mBundle.putBoolean(DetailShopFragment.FROM_TRANSACTION, transaction)
            profileShopFragment.arguments = mBundle
            setCurrentFragment(profileShopFragment,
                DetailShopFragment::class.java.simpleName)

        } else {
            var promo = false
            if (intent.hasExtra(EXTRA_PROMO)) {
                promo = intent.getBooleanExtra(EXTRA_PROMO, false)
            }
            val listShopFragment = ListShopFragment()
            val mBundle = Bundle()
            mBundle.putBoolean(ListShopFragment.EXTRA_PROMO, promo)
            listShopFragment.arguments = mBundle
            supportFragmentManager.commit {
                replace(R.id.host_listshop_activity, listShopFragment)
            }
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

    private fun setCurrentFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(R.id.host_listshop_activity, fragment, fragmentTag)
        }
    }

}