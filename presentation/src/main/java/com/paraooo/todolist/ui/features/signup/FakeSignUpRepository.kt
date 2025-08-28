package com.paraooo.todolist.ui.features.signup

import android.util.Log
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.remote.dto.request.SendVerificationCodeRequestDto
import com.paraooo.remote.dto.request.SignUpRequestDto
import com.paraooo.remote.dto.request.VerifyCodeRequestDto
import com.paraooo.remote.service.ApiService
import kotlinx.coroutines.delay

class FakeSignUpRepository(
    private val apiService: ApiService
) {

    suspend fun sendVerificationCode(email: String): UseCaseResult<Unit> {

        try{
            apiService.sendVerificationCode(SendVerificationCodeRequestDto(email))
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("PARAOOO", "login: ${e}")
            return UseCaseResult.Error(e)
        }
//        delay(1000L)

//        if(email == "nakim3159@gmail.com") {
//            return UseCaseResult.Success(Unit)
//        } else if(email == "nakim3159@gmail.co"){
//            return UseCaseResult.Failure(
//                "존재하지 않는 이메일입니다."
//            )
//        } else {
//            return UseCaseResult.Error(
//                Exception()
//            )
//        }
    }

    suspend fun verifyCode(email: String, code: String): UseCaseResult<Unit> {

        try{
            apiService.verifyCode(VerifyCodeRequestDto(email, code))
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("PARAOOO", "login: ${e}")
            return UseCaseResult.Error(e)
        }
//        delay(1000L)
//
//        if(code == "123456") {
//            return UseCaseResult.Success(Unit)
//        } else if(code == "000000") {
//            return UseCaseResult.Failure(
//                message = "인증 코드가 일치하지 않습니다."
//            )
//        }else {
//            return UseCaseResult.Error(
//                Exception()
//            )
//        }
    }

    suspend fun signUp(nickname: String, email: String, password: String): UseCaseResult<Unit> {
        try{
            apiService.signUp(SignUpRequestDto(email, password))
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            Log.e("PARAOOO", "login: ${e}")
            return UseCaseResult.Error(e)
        }
//        delay(1000L)
//        if(nickname == "nakim3159") {
//            return UseCaseResult.Success(Unit)
//        } else {
//            return UseCaseResult.Error(Exception())
//        }
    }
}