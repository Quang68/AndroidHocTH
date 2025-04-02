package com.example.contactapp.model

class Contact {
    private val id: Int
    private var name: String
    private var phone: String

    constructor(id: Int, name: String, phone: String) {
        this.id = id
        this.name = name
        this.phone = phone
    }
}