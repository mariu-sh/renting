package com.mariuszf.flatconfig.application.port.out

import com.mariuszf.flatconfig.application.service.Flat
import com.mariuszf.flatconfig.application.service.Room
import java.util.*

interface FlatConfigInfoSync {

    fun sendCreateCommand(flat: Flat)
    fun sendCreateCommand(room: Room)

    fun sendUpdateCommand(flat: Flat)
    fun sendUpdateCommand(room: Room)

    fun sendDeleteCommand(nodeId: UUID)
}
