package com.mariuszf.flatconfig.adapters.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class FlatNotFoundRestException(override val message: String?): RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class RoomNotFoundRestException(override val message: String?): RuntimeException(message)