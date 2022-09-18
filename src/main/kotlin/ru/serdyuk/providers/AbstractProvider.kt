package ru.serdyuk.providers

abstract class AbstractProvider(private val id: String, private val concurrentLevel: Int = 10) : Provider {
    override fun get(): String = id

    override fun getId(): String = id

    override fun check(): Boolean = true

    override fun concurrentLevel(): Int = concurrentLevel
}
