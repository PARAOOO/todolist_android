package com.paraooo.todolist.ui.features.start

import android.util.Log
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.remote.dto.request.LoginRequestDto
import com.paraooo.remote.service.SyncService
import kotlinx.coroutines.delay

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

class FakeLoginRepository(
    private val syncService: SyncService
) {

    suspend fun login(email: String, password: String) : UseCaseResult<Tokens> {

        try{
            val response = syncService.login(LoginRequestDto(email, password))

            if(response.body() == null) return UseCaseResult.Failure("실패실패")
            else {
                return UseCaseResult.Success(Tokens(
                    accessToken = response.body()!!.accessToken, refreshToken = response.body()!!.refreshToken
                ))
            }
        } catch (e: Exception) {
            Log.e("PARAOOO", "login: ${e}")
            return UseCaseResult.Error(e)
        }

//        delay(1000L)
//
//        if(email == "nakim3159@gmail.com" && password == "1234") {
//            return UseCaseResult.Success(Tokens(
//                accessToken = "asd", refreshToken = "asd"
//            ))
//        }
//        else if(email == "nakim3159@gmail.com" && password == "3159") {
//            return UseCaseResult.Success(Tokens(
//                accessToken = "", refreshToken = ""
//            ))
//        }
//        else if(email == "nakim3159") {
//            return UseCaseResult.Error(
//                exception = Exception()
//            )
//        }
//        else {
//            return UseCaseResult.Failure(
//                message = "아이디 또는 비밀번호를 확인해주세요"
//            )
//        }
    }

    suspend fun storeTokens(
        accessToken: String,
        refreshToken: String
    ) : UseCaseResult<Unit> {
        delay(500L)

        if(accessToken.isNotEmpty() && refreshToken.isNotEmpty()) {
            return UseCaseResult.Success(Unit)
        } else {
          return UseCaseResult.Error(
              exception = Exception()
          )
        }
    }
}