package com.mariuszf.flatconfig.adapters.postgres

class FlatNotFoundInStorageException(override val message: String?) : RuntimeException(message)
class RoomNotFoundInStorageException(override val message: String?): RuntimeException(message)