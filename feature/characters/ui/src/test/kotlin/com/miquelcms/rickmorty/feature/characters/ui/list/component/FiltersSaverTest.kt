package com.miquelcms.rickmorty.feature.characters.ui.list.component

import androidx.compose.runtime.saveable.SaverScope
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterFiltersUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterGenderUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterSpeciesUi
import com.miquelcms.rickmorty.feature.characters.ui.model.CharacterStatusUi
import org.junit.Assert.assertEquals
import org.junit.Test

class FiltersSaverTest {

    private val saverScope = SaverScope { true }

    @Test
    fun `restores every filter that was saved`() {
        val filters = CharacterFiltersUi(
            status = CharacterStatusUi.DEAD,
            gender = CharacterGenderUi.MALE,
            species = CharacterSpeciesUi.ALIEN,
        )

        assertEquals(filters, saveAndRestore(filters))
    }

    @Test
    fun `restores filters with nothing selected`() {
        val filters = CharacterFiltersUi()

        assertEquals(filters, saveAndRestore(filters))
    }

    private fun saveAndRestore(filters: CharacterFiltersUi): CharacterFiltersUi? {
        val saved = with(FiltersSaver) { saverScope.save(filters) }
        return saved?.let(FiltersSaver::restore)
    }
}
