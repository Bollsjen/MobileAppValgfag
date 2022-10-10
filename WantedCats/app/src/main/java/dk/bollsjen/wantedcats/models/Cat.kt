package dk.bollsjen.wantedcats.models

data class Cat (
    val id: Int,
    val name: String,
    val description: String,
    val place: String,
    val reward: Int,
    val userId: String,
    val date: Long,
    val pictureUrl: String
    )