package com.ronieapps.routing.response

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val uid: String,
    val email: String
)
