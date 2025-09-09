package com.paraooo.data.platform.logout

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface LogoutEffectProvider {
    val logoutEffectFlow: Flow<Unit>
    suspend fun triggerLogout()
}

class LogoutEffectProviderImpl(): LogoutEffectProvider{

    private val _logoutEffectChannel = Channel<Unit>(Channel.BUFFERED)
    override val logoutEffectFlow: Flow<Unit> = _logoutEffectChannel.receiveAsFlow()

    override suspend fun triggerLogout() {
        _logoutEffectChannel.send(Unit)
    }

}