package com.example.dragonballcompose.models

data class Character(
    val id: Int,
    val name: String,
    val ki: String,
    val maxKi: String,
    val race: String,
    val gender: String,
    val description: String,
    val image: String,
    val affiliation: String,
    val deletedAt: String?,
    val originPlanet: Planet?,
    val transformations: List<Transformation>?
)

data class Planet(
    val id: Int,
    val name: String,
    val isDestroyed: Boolean,
    val description: String,
    val image: String,
    val deletedAt: String?
)

data class Transformation(
    val id: Int,
    val name: String,
    val image: String,
    val ki: String,
    val deletedAt: String?
)

data class CharactersResponse(
    val items: List<Character>,
    val meta: Meta,
    val links: Links?
)

data class Meta(
    val totalItems: Int,
    val itemCount: Int,
    val itemsPerPage: Int,
    val totalPages: Int,
    val currentPage: Int
)

data class Links(
    val first: String?,
    val previous: String?,
    val next: String?,
    val last: String?
)

data class UserProfile(
    val uid: String = "",
    val nombre: String = "",
    val apellidoPaterno: String = "",
    val apellidoMaterno: String = "",
    val username: String = "",
    val email: String = ""
)
