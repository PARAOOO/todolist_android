package com.paraooo.todolist.ui.features.signup

sealed class VerificationState {
    object NONE : VerificationState()
    object WAITING : VerificationState()
    object SUCCESS : VerificationState()
    object EXPIRED : VerificationState()
}

data class GetVerificationCodeButtonState(
    val isEnabled: Boolean = false,
    val verificationState: VerificationState = VerificationState.NONE
)

data class SignUpUiState (
    val nickname: String = "",
    val nicknameErrorMessage: String? = null,

    val email: String = "",
    val emailErrorMessage: String? = null,

    val password: String = "",
    val passwordErrorMessage: String? = null,

    val passwordCheck: String = "",
    val passwordCheckErrorMessage: String? = null,

    val verificationCode: String = "",
    val verificationCodeErrorMessage: String? = null,

    val getVerificationCodeButtonState: GetVerificationCodeButtonState = GetVerificationCodeButtonState()

) {

    val isNicknameLengthValid: Boolean
        get() {
            return nickname.length >= 2 && nickname.length <= 10
        }

    val isNicknameAlphabetKoreanDigitOnly: Boolean
        get() {
            return nickname.matches(Regex("^[a-zA-Z가-힣0-9]+\$"))
        }

    val isNicknameValid: Boolean
        get() {
            return isNicknameLengthValid && isNicknameAlphabetKoreanDigitOnly
        }

    val isEmailFormatValid: Boolean
        get() {
            return email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"))
        }

    val isEmailValid: Boolean
        get() {
            return isEmailFormatValid
        }

    val isPasswordLengthValid: Boolean
        get() {
            return password.length >= 8 && password.length <= 20
        }

    val isPasswordAlphabetDigitSpecialCharOnly: Boolean
        get() {
            return password.matches(Regex("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$"))
        }

    val isPasswordContainAlphabetDigitSpecialChar: Boolean
        get() {
            return password.any { it.isLetter() } && password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() }
        }

    val isPasswordValid: Boolean
        get() {
            return isPasswordLengthValid && isPasswordContainAlphabetDigitSpecialChar && isPasswordAlphabetDigitSpecialCharOnly
        }

    val isPasswordCheckValid: Boolean
        get() {
            return password == passwordCheck
        }

    val isSignUpButtonEnabled: Boolean
        get() {
            return isNicknameValid && isEmailValid && isPasswordValid && isPasswordCheckValid && getVerificationCodeButtonState.verificationState == VerificationState.SUCCESS

        }
}