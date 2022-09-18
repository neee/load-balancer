package ru.serdyuk.providers

interface Provider {
    fun get(): String

    fun getId(): String

    fun check(): Boolean

    fun concurrentLevel(): Int
}
