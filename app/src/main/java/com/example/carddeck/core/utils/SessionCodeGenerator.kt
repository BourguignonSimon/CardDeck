package com.example.carddeck.core.utils

import javax.inject.Inject
import kotlin.random.Random

class SessionCodeGenerator @Inject constructor() {
    private val alphabet = (('A'..'Z') + ('0'..'9')).toTypedArray()

    fun generateCode(length: Int = 6): String {
        return buildString {
            repeat(length) {
                append(alphabet[Random.nextInt(alphabet.size)])
            }
        }
    }
}
