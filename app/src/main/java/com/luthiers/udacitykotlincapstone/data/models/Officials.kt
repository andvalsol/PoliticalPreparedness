package com.luthiers.udacitykotlincapstone.data.models

import com.squareup.moshi.Json

data class Officials(
    @field:Json(name = "officials") val officials: List<SingleRepresentative>
)