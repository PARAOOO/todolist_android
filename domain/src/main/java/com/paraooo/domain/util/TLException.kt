package com.paraooo.domain.util

import java.io.IOException

sealed class TLException(message: String): IOException(message)

class NetworkException(message: String = "네트워크 연결을 확인해주세요.") : TLException(message)

class ServerErrorException(message: String = "서버에 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.") : TLException(message)

class UnauthorizeException(message: String = "다시 로그인해주세요.") : TLException(message)

class ForbiddenException(message: String = "접근 권한이 없습니다.") : TLException(message)

class BadRequestException(message: String = "잘못된 요청입니다.") : TLException(message)

class DataEmptyException(message: String = "데이터가 비어있습니다.") : TLException(message)

class UnknownHttpException(val code: Int, message: String) : TLException(message)