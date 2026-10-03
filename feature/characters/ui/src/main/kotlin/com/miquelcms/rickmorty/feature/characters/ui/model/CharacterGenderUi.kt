package com.miquelcms.rickmorty.feature.characters.ui.model

import androidx.annotation.StringRes
import com.miquelcms.rickmorty.feature.characters.ui.R

enum class CharacterGenderUi(@StringRes val labelRes: Int) {
    FEMALE(R.string.character_gender_female),
    MALE(R.string.character_gender_male),
    GENDERLESS(R.string.character_gender_genderless),
    UNKNOWN(R.string.character_gender_unknown),
}
