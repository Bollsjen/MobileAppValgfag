package dk.bollsjen.wantedcats.models

import java.time.LocalDate
import java.util.*

data class Cat (
    val id: Int,
    val name: String,
    val description: String,
    val place: String,
    val reward: Int,
    val userId: Int,
    val date: Long,
    val pictureUrl: String
    )