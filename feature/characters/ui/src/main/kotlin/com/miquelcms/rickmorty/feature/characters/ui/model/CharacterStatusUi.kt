package com.miquelcms.rickmorty.feature.characters.ui.model

import androidx.annotation.StringRes
import com.miquelcms.rickmorty.core.designsystem.component.RmStatusType
import com.miquelcms.rickmorty.feature.characters.ui.R

enum class CharacterStatusUi(
    @StringRes val labelRes: Int,
    val type: RmStatusType,
) {
    ALIVE(R.string.character_status_alive, RmStatusType.POSITIVE),
    DEAD(R.string.character_status_dead, RmStatusType.NEGATIVE),
    UNKNOWN(R.string.character_status_unknown, RmStatusType.NEUTRAL),
}
