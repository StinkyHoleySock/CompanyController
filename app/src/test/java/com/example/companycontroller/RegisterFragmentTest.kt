package com.example.companycontroller

import com.example.companycontroller.ui.fragments.login.RegisterFragment
import org.junit.Assert.*
import org.junit.Test

class RegisterFragmentTest {

    @Test
    fun `empty username returns false`() {
        val result = RegisterFragment().validateUser(
            "",
            "Pervov",
            "piervov00@gmail.com",
            "123456"
        )
        assertFalse(result)
    }

    @Test
    fun `password is valid`() {
        val result = RegisterFragment().validatePassword(
            "qwerty"
        )
        assertFalse(result)
    }

    @Test
    fun `rules checker is accepted`() {
        val result = RegisterFragment().checkRules(true)
        assertEquals("Rules is accepted", result)
    }

}

