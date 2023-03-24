package org.sirekanyan.knigopis.repository.cache

enum class CacheKey {

    BOOKS, USERS, NOTES;

    val storeValue get() = name.lowercase()

}