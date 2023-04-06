package com.example.companycontroller.data.model

//Класс модели данных для группы
data class Group(
    val id: String,
    val name: String,
    val task: String,
    val leader: String?,
    val members: MutableList<String>?
) {
    constructor() : this("", "", "", "", mutableListOf())
}
