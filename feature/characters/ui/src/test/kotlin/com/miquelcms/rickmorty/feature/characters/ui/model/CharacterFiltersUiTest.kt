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
            species = CharacterSpeciesUi.HUMAN,
        )

        assertEquals(3, filters.activeCount)
    }

    @Test
    fun `counts only the filters that are set`() {
        val filters = CharacterFiltersUi(species = CharacterSpeciesUi.ROBOT)

        assertEquals(1, filters.activeCount)
    }
}
