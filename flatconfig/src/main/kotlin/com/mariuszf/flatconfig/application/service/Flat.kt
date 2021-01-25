package com.mariuszf.flatconfig.application.service

import java.util.*

class Flat(val id: UUID, val totalSurface: Double) {
    var rooms: List<Room> = emptyList()
    var commonPartSurface = totalSurface

    fun updateCommonPartSurface(){
        commonPartSurface = totalSurface - sumRoomsSurface()
    }
    private fun sumRoomsSurface(): Double = rooms.sumByDouble { it.surface }

}