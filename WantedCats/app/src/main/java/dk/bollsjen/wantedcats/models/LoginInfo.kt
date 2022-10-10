package dk.bollsjen.wantedcats.models

data class LoginInfo
    (
        val id: Int,
        val email: String,
        val password: String
    )