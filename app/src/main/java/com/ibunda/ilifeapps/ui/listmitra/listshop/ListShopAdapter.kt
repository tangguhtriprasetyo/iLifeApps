package com.ibunda.ilifeapps.ui.listmitra.listshop

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibunda.ilifeapps.data.model.Shops
import com.ibunda.ilifeapps.data.model.Users
import com.ibunda.ilifeapps.databinding.ItemRvListShopBinding
import kotlin.math.roundToInt

class ListShopAdapter : RecyclerView.Adapter<ListShopViewHolder>() {
    private var listShops = ArrayList<Shops>()
    private var userData = Users()

    fun setListShops(shops: List<Shops>?, user: Users, filter: String?) {
        if (shops == null) return
        this.userData = user
        this.listShops.clear()
        for (item in shops) {
            val userLocation = Location("userLocation")
            if (userData.latitude != null || userData.longitude != null) {
                userLocation.latitude = userData.latitude!!
                userLocation.longitude = userData.longitude!!

                val shopLocation = Location("shopLocation")
                shopLocation.latitude = item.latitude!!
                shopLocation.longitude = item.longitude!!

                val distance: Int = (userLocation.distanceTo(shopLocation)).roundToInt()
                item.distance = distance
            }
            this.listShops.add(item)
        }
        when (filter) {
            "Distance" -> this.listShops.sortBy { it.distance }
            "Rating" -> this.listShops.sortByDescending { it.rating }
            "Popularitas" -> this.listShops.sortByDescending { it.totalPesananSukses }
        }
        notifyDataSetChanged()
    }

    fun setFilter(shops: List<Shops>?, user: Users) {
        if (shops == null) return
        this.listShops.clear()
        for (item in shops) {
            val userLocation = Location("userLocation")
            userLocation.latitude = userData.latitude!!
            userLocation.longitude = userData.longitude!!

            val shopLocation = Location("shopLocation")
            shopLocation.latitude = item.latitude!!
            shopLocation.longitude = item.longitude!!

            var distance: Int = (userLocation.distanceTo(shopLocation)).roundToInt()
            item.distance = distance
            this.listShops.add(item)
        }
        this.listShops.sortBy { it.distance }
        this.userData = user
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListShopViewHolder {
        val itemRvListShopBinding =
            ItemRvListShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListShopViewHolder(itemRvListShopBinding)
    }

    override fun onBindViewHolder(holder: ListShopViewHolder, position: Int) {
        val shops = listShops[position]
        holder.bind(shops, userData)
    }

    override fun getItemCount(): Int = listShops.size


}