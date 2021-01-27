package com.mariuszf.flatconfig.application.service

open class FlatConfigServiceTestBase {

    protected val flatStorage = InMemoryFlatStorage()
    protected val roomStorage = InMemoryRoomStorage()

    protected val flatConfigService = FlatConfigService(flatStorage, roomStorage)

}