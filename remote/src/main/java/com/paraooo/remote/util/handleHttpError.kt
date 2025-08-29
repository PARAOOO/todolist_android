package com.paraooo.remote.util

import com.paraooo.domain.util.BadRequestException
import com.paraooo.domain.util.ForbiddenException
import com.paraooo.domain.util.ServerErrorException
import com.paraooo.domain.util.TLException
import com.paraooo.domain.util.UnauthorizeException
import com.paraooo.domain.util.UnknownHttpException

const val TAG = "PARAOOO"

internal fun handleHttpError(code: Int): TLException {
    return when (code) {
        400 -> BadRequestException()
        401 -> UnauthorizeException()
        403 -> ForbiddenException()
        in 500..599 -> ServerErrorException()
        else -> UnknownHttpException(code, "알 수 없는 오류가 발생했습니다.")
    }
}
