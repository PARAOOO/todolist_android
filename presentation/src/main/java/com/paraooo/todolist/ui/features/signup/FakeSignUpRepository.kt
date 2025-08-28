package com.paraooo.todolist.ui.features.signup

import com.paraooo.domain.model.UseCaseResult
import kotlinx.coroutines.delay

class FakeSignUpRepository {

    suspend fun sendVerificationCode(email: String): UseCaseResult<Unit> {
        delay(1000L)

        if(email == "nakim3159@gmail.com") {
            return UseCaseResult.Success(Unit)
        } else if(email == "nakim3159@gmail.co"){
            return UseCaseResult.Failure(
                "존재하지 않는 이메일입니다."
            )
        } else {
            return UseCaseResult.Error(
                Exception()
            )
        }
    }

    suspend fun verifyCode(email: String, code: String): UseCaseResult<Unit> {
        delay(1000L)

        if(code == "123456") {
            return UseCaseResult.Success(Unit)
        } else if(code == "000000") {
            return UseCaseResult.Failure(
                message = "인증 코드가 일치하지 않습니다."
            )
        }else {
            return UseCaseResult.Error(
                Exception()
            )
        }
    }

    suspend fun signUp(nickname: String, email: String, password: String): UseCaseResult<Unit> {
        delay(1000L)
        if(nickname == "nakim3159") {
            return UseCaseResult.Success(Unit)
        } else {
            return UseCaseResult.Error(Exception())
        }
    }
}