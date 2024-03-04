package com.ronieapps.routing

import com.ronieapps.dao.DAOUser
import com.ronieapps.model.User
import com.ronieapps.routing.request.UserRequest
import com.ronieapps.routing.response.UserResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val daoUser = DAOUser()

    routing {
        get("/") {
            call.respond("Database H2 v1")
        }
        route("/api/auth") {
            post("/create") {
                val request = call.receive<UserRequest>()

                val user = User(
                    email = request.email,
                    password = request.password
                )

                val foundUser = daoUser.addNewUser(user)
                    ?: return@post call.respond(HttpStatusCode.BadRequest)

                call.respond(foundUser.toResponse())
            }
            get("/{uid?}") {
                val uid = call.parameters["uid"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                val foundUser = daoUser.queryUser(uid)
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                call.respond(foundUser.toResponse())
            }
        }

    }
}

fun User.toResponse(): UserResponse =
    UserResponse(
        uid = this.uid!!,
        email = this.email
    )