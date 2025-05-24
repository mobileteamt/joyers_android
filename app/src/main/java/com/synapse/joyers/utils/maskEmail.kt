package com.synapse.joyers.utils

fun maskEmail(email: String): String {
    val atIndex = email.indexOf('@')
    if (atIndex <= 1) return email
    val firstChar = email[0]
    val domain = email.substring(atIndex)
    return "$firstChar*****$domain"
}

fun maskPhone(phone: String): String {
    if (phone.length < 6) return phone
    val start = phone.substring(0, 4)
    val end = phone.takeLast(2)
    return "$start********$end"
}