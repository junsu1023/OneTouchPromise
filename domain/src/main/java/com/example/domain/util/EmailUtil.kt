package com.example.domain.util

import android.util.Patterns

fun String.isNotValidEmail(): Boolean = !Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isShorterThanLength2(): Boolean = this.length < 2

fun String.isNotValidateNickname(): Boolean {
    val regex = Regex("^[가-힣a-zA-Z0-9]{2,12}\$")
    return !this.matches(regex)
}

fun String.isShorterThanLength8(): Boolean = this.length < 8