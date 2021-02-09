package com.mariuszf.flatconfig.adapters.messaging

import com.mariuszf.flatconfig.application.port.out.FlatConfigInfoSync
import com.mariuszf.flatconfig.application.service.Flat
import com.mariuszf.flatconfig.application.service.Room
import org.springframework.cloud.stream.function.StreamBridge
import java.util.*

class FlatConfigInfoSyncImpl(private val streamBridge: StreamBridge) : FlatConfigInfoSync {

    private val topicBinderName = "flatConfigInfo-out-0"

    override fun sendCreateCommand(flat: Flat) {
        send(MessagingDTO.fromDomain(flat, MessageType.CREATE))
    }

    override fun sendCreateCommand(room: Room) {
        send(MessagingDTO.fromDomain(room, MessageType.CREATE))
    }

    override fun sendUpdateCommand(flat: Flat) {
        send(MessagingDTO.fromDomain(flat, MessageType.UPDATE))
    }

    override fun sendUpdateCommand(room: Room) {
        send(MessagingDTO.fromDomain(room, MessageType.UPDATE))
    }

    override fun sendDeleteCommand(nodeId: UUID) {
        send(MessagingDTO(nodeId, messageType = MessageType.DELETE))
    }

    private fun send(messagingDTO: MessagingDTO) {
        streamBridge.send(topicBinderName, messagingDTO)
    }

    data class MessagingDTO(
        val id: UUID,
        val surface: Double? = null,
        val parentId: UUID? = null,
        val messageType: MessageType) {
        companion object{
            @JvmStatic
            fun fromDomain(flat: Flat, messageType: MessageType) =
                MessagingDTO(flat.id, flat.totalSurface, null, messageType)
            @JvmStatic
            fun fromDomain(room: Room, messageType: MessageType) =
                MessagingDTO(room.id, room.surface, room.flatId, messageType)
        }
    }

    enum class MessageType{
        CREATE, UPDATE, DELETE
    }
}
