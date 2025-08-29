package com.paraooo.domain.usecase.auth

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.AuthRepository
import com.paraooo.domain.util.BadRequestException

class SendVerificationCodeUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): UseCaseResult<Unit> {
        try{
            val response = authRepository.sendVerificationCode(email)
            return UseCaseResult.Success(response)
        } catch(e: BadRequestException) {
            return UseCaseResult.Failure("잘못된 요청입니다.")
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}