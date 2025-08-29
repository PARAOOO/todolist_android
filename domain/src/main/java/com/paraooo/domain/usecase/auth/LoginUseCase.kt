package com.paraooo.domain.usecase.auth

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.AuthRepository
import com.paraooo.domain.util.UnauthorizeException

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): UseCaseResult<Pair<String, String>> {
        try{
            val response = authRepository.login(email, password)
            return UseCaseResult.Success(response)
        } catch (e: UnauthorizeException){
            return UseCaseResult.Failure("아이디 또는 비밀번호를 확인해주세요")
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}

