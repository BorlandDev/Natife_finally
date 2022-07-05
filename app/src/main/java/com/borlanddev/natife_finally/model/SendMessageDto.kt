package model

data class SendMessageDto(val id: String, val receiver: String, val message: String) : Payload