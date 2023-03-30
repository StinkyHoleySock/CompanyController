package com.example.companycontroller.data.model

import java.util.UUID

//Класс модели данных для пользователя
data class User(
    val id: UUID,
    val name: String,
    val surname: String,
    val patronymic: String,
    val isEmployee: Boolean
)