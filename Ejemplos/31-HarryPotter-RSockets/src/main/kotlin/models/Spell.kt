package models

import kotlinx.serialization.Serializable

@Serializable
data class Spell(
    val id: Int,
    val name: String,
    val type: Type,
) {
    enum class Type {
        ATTACK,
        DEFENSE,
        HEAL,
        BUFF,
        DEBUFF,
        NONE
    }
}