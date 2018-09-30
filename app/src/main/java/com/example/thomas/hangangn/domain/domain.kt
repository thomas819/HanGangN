package com.example.thomas.hangangn.domain

data class Place(
        var img: Int? = null,
        var name: String? = null,
        var placeName:String?=null
)
data class Filters(
        var placeName:String?=null
)