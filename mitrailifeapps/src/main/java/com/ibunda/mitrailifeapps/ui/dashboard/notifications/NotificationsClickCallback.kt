package com.ibunda.mitrailifeapps.ui.dashboard.notifications

import com.ibunda.mitrailifeapps.data.model.Notifications

interface NotificationsClickCallback {
    fun onItemClicked(data: Notifications)
}