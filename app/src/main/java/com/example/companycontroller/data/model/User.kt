package com.example.companycontroller.data.model

//Класс модели данных для пользователя
data class User(
    val id: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    val isLeader: Boolean,
    val isSuperUser: Boolean
) {
    /**В данном случае добавлен вторичный конструктор без аргументов, который вызывает
     * первый конструктор и передает в него пустые значения.
     * Таким образом, Firebase сможет создать объект User без аргументов и заполнить
     * его поля значениями из Firestore.**/

    constructor() : this("", "", "", "", "", false, false)
}