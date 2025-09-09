package com.paraooo.todolist.ui.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraooo.data.platform.sync.SyncPullScheduler
import com.paraooo.domain.repository.AuthRepository
import com.paraooo.domain.repository.SyncRepository
import com.paraooo.todolist.ui.features.home.HomeUiEffect
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

const val SPLASH_DELAY_MS = 2000L

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val syncPullScheduler: SyncPullScheduler
): ViewModel() {

    private val _effectChannel = Channel<SplashUiEffect>()
    val effectFlow = _effectChannel.receiveAsFlow()

    init {
        decideNextScreen()
    }

    private fun decideNextScreen() {
        viewModelScope.launch {
            val isLoggedInDeferred: Deferred<Boolean> = async(Dispatchers.IO) {
                val isLoggedIn = authRepository.isLoggedIn()

                if(isLoggedIn) {
                    syncPullScheduler.runSyncPullWorker()
                }

                isLoggedIn
            }

            val delayJob = launch {
                delay(SPLASH_DELAY_MS)
            }

            delayJob.join()
            val isLoggedIn = isLoggedInDeferred.await()

            if (isLoggedIn) {
                _effectChannel.send(SplashUiEffect.NaviagateToHome)
            } else {
                _effectChannel.send(SplashUiEffect.NaviagateToLogin)
            }
        }
    }
}