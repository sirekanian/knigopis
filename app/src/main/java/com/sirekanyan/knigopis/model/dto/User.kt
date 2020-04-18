package com.sirekanyan.knigopis.model.dto

class User(
    val id: String,
    val nickname: String?,
    val photo: String?,
    val profile: String?,
    private val identity: String?,
    val booksCount: Int
) {
    val name get() = nickname ?: id
    val profiles get() = listOfNotNull(profile, identity)
}