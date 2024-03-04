package com.ronieapps.plugins

import com.ronieapps.model.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val driverClassName = environment.config.property("storage.driverClassName").getString()
    val jdbcURL = environment.config.property("storage.jdbcURL").getString()
    val db = Database.connect(provideDataSource(jdbcURL, driverClassName))
    transaction(db) {
        SchemaUtils.create(Users)
    }
}

fun provideDataSource(url: String, driverClass: String): HikariDataSource {
    val hikariConfig = HikariConfig().apply {
        jdbcUrl = url
        driverClassName = driverClass
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    return HikariDataSource(hikariConfig)
}