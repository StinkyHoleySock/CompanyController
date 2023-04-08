package com.example.companycontroller.data.model

//Класс модели данных для группы
data class Group(
    val id: String,
    val name: String,
    val task: String,
    val leader: String?,
    val members: MutableList<String>?
) {
    /**В данном случае добавлен вторичный конструктор без аргументов, который вызывает
     * первичный конструктор и передает в него пустые значения.
     * Таким образом, Firebase сможет создать объект Group без аргументов и заполнить
     * его поля значениями из Firestore.**/
    constructor() : this("", "", "", "", mutableListOf())
}
