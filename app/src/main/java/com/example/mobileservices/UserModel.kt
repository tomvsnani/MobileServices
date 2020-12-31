package com.example.mobileservices

data class UserModel(
    var first_name: String = "",
    var last_name: String = "",
    var email_id: String = "",
    var mobile_number: String = "",
    var id: Int = -1,
    var password: String = ""
) {
}