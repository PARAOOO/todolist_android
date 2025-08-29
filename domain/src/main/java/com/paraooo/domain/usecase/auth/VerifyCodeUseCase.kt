package com.paraooo.domain.usecase.auth

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.AuthRepository
import com.paraooo.domain.util.BadRequestException
import com.paraooo.domain.util.TLException

class VerifyCodeUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String): UseCaseResult<Unit> {
        try{
            val response = authRepository.verifyCode(email, code)
            return UseCaseResult.Success(response)
        } catch (e: BadRequestException) {
            return UseCaseResult.Failure("잘못된 요청입니다.")
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}