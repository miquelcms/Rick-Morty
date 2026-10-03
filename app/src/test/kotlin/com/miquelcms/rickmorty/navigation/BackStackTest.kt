package com.miquelcms.rickmorty.navigation

import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Test

class BackStackTest {

    private val backStack = mutableListOf<NavKey>(CharacterListKey)

    @Test
    fun `opens the detail of a character on top of the list`() {
        backStack.openCharacterDetail(characterId = 7)

        assertEquals(listOf(CharacterListKey, CharacterDetailKey(7)), backStack)
    }

    @Test
    fun `does not open another detail when one is already open`() {
        backStack.openCharacterDetail(characterId = 7)

        backStack.openCharacterDetail(characterId = 8)

        assertEquals(listOf(CharacterListKey, CharacterDetailKey(7)), backStack)
    }

    @Test
    fun `going back closes the detail`() {
        backStack.openCharacterDetail(characterId = 7)

        backStack.goBack()

        assertEquals(listOf<NavKey>(CharacterListKey), backStack)
    }

    @Test
    fun `going back never removes the first screen`() {
        backStack.goBack()

        assertEquals(listOf<NavKey>(CharacterListKey), backStack)
    }
}
