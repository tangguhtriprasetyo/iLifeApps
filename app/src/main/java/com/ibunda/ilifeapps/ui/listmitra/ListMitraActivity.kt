package com.ibunda.ilifeapps.ui.listmitra

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ibunda.ilifeapps.R
import com.ibunda.ilifeapps.data.model.ChatRoom
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ActivityListMitraBinding
import com.ibunda.ilifeapps.ui.listmitra.listshop.ListShopFragment
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.DetailShopFragment
import com.ibunda.ilifeapps.ui.listmitra.listshop.detailshop.payment.PaymentFragment

class ListMitraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMitraBinding

    private val listMitraViewModel: ListMitraViewModel by viewModels()
    private lateinit var user: Users
    private var chatRoom: ChatRoom? = null
    private var shopData = Shops()
    private var userId: String? = null
    private var search: String? = null

    companion object {
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
        const val EXTRA_USER = "extra_user"
        const val EXTRA_SHOP = "extra_shop"
        const val EXTRA_PROMO = "extra_promo"
        const val EXTRA_SEARCH = "extra_search"
        const val EXTRA_TRANSACTION = "extra_transaction"
        const val EXTRA_ORDER_AGAIN = "extra_order_again"
        const val EXTRA_ORDER_FROM_CHAT = "extra_order_from_chat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName: String? = intent.getStringExtra(EXTRA_CATEGORY_NAME)
        search = intent.getStringExtra(EXTRA_SEARCH)
        Log.d(categoryName, "categoryName")
        if (categoryName != null) {
            listMitraViewModel.setCategory(categoryName)
        }

        if (intent.hasExtra(EXTRA_USER)) {
            userId = intent.getStringExtra(EXTRA_USER)
            setUserProfile(userId)
        }

        if (intent.hasExtra(EXTRA_SHOP)) {
            val shopId = intent.getStringExtra(EXTRA_SHOP)
            setShopData(shopId)

        } else if (intent.hasExtra(EXTRA_ORDER_FROM_CHAT)) {
            chatRoom = intent.getParcelableExtra(EXTRA_ORDER_FROM_CHAT)
            setUserProfile(chatRoom?.userId)
            setShopData(chatRoom?.shopId)
            val paymentFragment = PaymentFragment()
            setCurrentFragment(
                paymentFragment,
                paymentFragment::class.java.simpleName
            )

        } else {
            var promo = false
            if (intent.hasExtra(EXTRA_PROMO)) {
                promo = intent.getBooleanExtra(EXTRA_PROMO, false)
            }
            val listShopFragment = ListShopFragment()
            val mBundle = Bundle()
            mBundle.putBoolean(ListShopFragment.EXTRA_PROMO, promo)
            mBundle.putString(ListShopFragment.EXTRA_SEARCH, search)
            listShopFragment.arguments = mBundle
            supportFragmentManager.commit {
                replace(R.id.host_listshop_activity, listShopFragment)
            }
        }
    }

    private fun setShopData(shopId: String?) {
        val transaction = intent.getBooleanExtra(EXTRA_TRANSACTION, false)
        listMitraViewModel.setShopData(shopId.toString())
            .observe(this, { shops ->
                if (shops != null) {
                    shopData = shops

                    if (intent.hasExtra(EXTRA_ORDER_AGAIN)) {
                        val paymentFragment = PaymentFragment()
                        setCurrentFragment(
                            paymentFragment,
                            paymentFragment::class.java.simpleName
                        )
                    } else {
                        val profileShopFragment = DetailShopFragment()
                        shopData.shopId = shopId

                        val mBundle = Bundle()
                        mBundle.putParcelable(DetailShopFragment.EXTRA_SHOP_DATA, shopData)
                        mBundle.putBoolean(DetailShopFragment.FROM_TRANSACTION, transaction)
                        profileShopFragment.arguments = mBundle
                        setCurrentFragment(
                            profileShopFragment,
                            DetailShopFragment::class.java.simpleName
                        )
                    }
                }
            })
    }

    private fun setUserProfile(userId: String?) {
        listMitraViewModel.setUserProfile(userId.toString()).observe(this, { userProfile ->
            if (userProfile != null) {
                user = userProfile
            }
        })

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