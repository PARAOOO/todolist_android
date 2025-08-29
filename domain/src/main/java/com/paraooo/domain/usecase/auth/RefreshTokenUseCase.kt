package com.paraooo.domain.usecase.auth

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.AuthRepository
import com.paraooo.domain.util.ForbiddenException

class RefreshTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String): UseCaseResult<Pair<String, String>> {
        try{
            val response = authRepository.refreshToken(refreshToken)
            return UseCaseResult.Success(response)
        } catch(e: ForbiddenException) {
            return UseCaseResult.Failure("다시 로그인해주세요.")
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}
