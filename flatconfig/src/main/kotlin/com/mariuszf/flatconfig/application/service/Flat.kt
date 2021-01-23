package com.mariuszf.flatconfig.application.service

import java.util.*

class Flat(val id: UUID, val totalSurface: Double) {
    val rooms: MutableList<Room> = emptyList<Room>() as MutableList<Room>
    var commonPartSurface = totalSurface


    private fun updateCommonPartSurface(){
        commonPartSurface = totalSurface - sumRoomsSurface()
    }
    private fun sumRoomsSurface(): Double = rooms.sumByDouble { it.surface }

}