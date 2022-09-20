package dk.bollsjen.wantedcats.models

import java.util.*

data class Cat (val id: Int, val description: String, val place: String, val reward: Int, val userId: Int, val data: Date, val pictureUrl: String)