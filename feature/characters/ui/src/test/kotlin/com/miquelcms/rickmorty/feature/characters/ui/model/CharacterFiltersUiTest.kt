package com.miquelcms.rickmorty.feature.characters.ui.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterFiltersUiTest {

    @Test
    fun `no filter is active by default`() {
        assertEquals(0, CharacterFiltersUi().activeCount)
    }

    @Test
    fun `counts every filter that is set`() {
        val filters = CharacterFiltersUi(
            status = CharacterStatusUi.ALIVE,
            gender = CharacterGenderUi.FEMALE,
            species = "Human",
            type = "Clone",
        )

        assertEquals(4, filters.activeCount)
    }

    @Test
    fun `does not count blank texts`() {
        val filters = CharacterFiltersUi(status = CharacterStatusUi.DEAD, species = "  ", type = "")

        assertEquals(1, filters.activeCount)
    }
}
