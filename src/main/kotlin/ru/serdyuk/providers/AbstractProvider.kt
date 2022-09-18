package ru.serdyuk.providers

abstract class AbstractProvider(private val id: String) : Provider {
    override fun get(): String = id
    override fun getId(): String = id
    override fun check(): Boolean = true
}
