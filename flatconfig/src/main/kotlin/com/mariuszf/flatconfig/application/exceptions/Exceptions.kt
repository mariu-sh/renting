package com.mariuszf.flatconfig.application.exceptions

class FlatNotFoundException(override val message: String?): RuntimeException(message)
class RoomNotFoundException(override val message: String?): RuntimeException(message)

class CommonPartIsNegativeException(override val message: String?): RuntimeException(message)
class RoomSurfaceIsNegativeException(override val message: String?): RuntimeException(message)