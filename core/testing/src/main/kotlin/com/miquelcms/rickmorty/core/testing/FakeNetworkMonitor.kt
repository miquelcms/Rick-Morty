package com.miquelcms.rickmorty.core.testing

import com.miquelcms.rickmorty.core.domain.network.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow

class FakeNetworkMonitor(isOnline: Boolean = true) : NetworkMonitor {
    override val isOnline = MutableStateFlow(isOnline)
}
