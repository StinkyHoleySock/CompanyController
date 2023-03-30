package com.example.companycontroller.data.model

import java.util.UUID

//Класс модели данных для группы
data class Group(
    val id: UUID,
    val name: String,
    val members: List<User>,
    val task: String
)
