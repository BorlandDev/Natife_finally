package com.borlanddev.domain.model

data class MessageDto(val from: User, val message: String) : Payload