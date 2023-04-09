package com.example.companycontroller.data.model

data class Message(
    var id: String,
    val sender: String,
    val data: String
) {
    /**В данном случае добавлен вторичный конструктор без аргументов, который вызывает
     * первичный конструктор и передает в него пустые значения.
     * Таким образом, Firebase сможет создать объект Message без аргументов и заполнить
     * его поля значениями из Firestore.**/
    constructor() : this("", "", "")
}