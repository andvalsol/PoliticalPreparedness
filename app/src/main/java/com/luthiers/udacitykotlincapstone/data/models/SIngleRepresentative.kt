package com.luthiers.udacitykotlincapstone.data.models

import com.squareup.moshi.Json

data class SingleRepresentative(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "party") val party: String,
    @field:Json(name = "photoUrl") val photoUrl: String
)