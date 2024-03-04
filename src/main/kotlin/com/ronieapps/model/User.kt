package com.ronieapps.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val uid: String? = null,
    val email: String,
    val password: String
)

object Users : Table() {
    val uid = varchar("uid", 1024)
    val email = varchar("email", 128)
    val password = varchar("password", 128)

    override val primaryKey = PrimaryKey(uid)
}