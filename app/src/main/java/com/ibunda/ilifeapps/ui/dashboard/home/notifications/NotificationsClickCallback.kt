package com.ibunda.ilifeapps.ui.dashboard.home.notifications

import com.ibunda.ilifeapps.data.model.Notifications

interface NotificationsClickCallback {
    fun onItemClicked(data: Notifications)
}