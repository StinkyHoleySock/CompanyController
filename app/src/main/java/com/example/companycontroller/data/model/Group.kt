package com.example.companycontroller.data.model

import java.util.UUID

//Класс модели данных для группы
data class Group(
    val id: String,
    val name: String,
    val members: MutableList<String>,
    val task: String
) {
    constructor() : this("", "", mutableListOf(), "")
}
