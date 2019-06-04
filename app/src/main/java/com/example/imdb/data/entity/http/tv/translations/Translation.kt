package com.example.imdb.data.entity.http.tv.translations


import com.google.gson.annotations.SerializedName

data class Translation(
    @SerializedName("data")
    val translationData: Data,
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    val name: String
)