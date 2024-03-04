package com.ronieapps.dao

import com.ronieapps.model.User
import com.ronieapps.model.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class DAOUser {
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun resultRowToUser(row: ResultRow) = User(
        uid = row[Users.uid],
        email = row[Users.email],
        password = row[Users.password]
    )

    suspend fun addNewUser(user: User): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.uid] = UUID.randomUUID().toString()
            it[Users.email] = user.email
            it[Users.password] = user.password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    suspend fun queryUser(uid: String): User? = dbQuery {
        Users.select {
            Users.uid eq uid
        }.map(::resultRowToUser).singleOrNull()
    }
}