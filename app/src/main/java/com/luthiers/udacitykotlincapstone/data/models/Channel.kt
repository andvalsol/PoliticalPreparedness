package com.luthiers.udacitykotlincapstone.data.models

import com.squareup.moshi.Json

data class Channel(
    @field:Json(name = "type") val type: String,
    @field:Json(name = "id") val id: String
)