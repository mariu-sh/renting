package com.mariuszf.flatconfig.application.exceptions

class FlatNotFoundException(override val message: String?): RuntimeException(message)
class RoomNotFoundException(override val message: String?): RuntimeException(message)

class SurfaceIsInvalidException(override val message: String?): RuntimeException(message)