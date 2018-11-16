package uno.enums

enum class Direction {

    FORWARDS, BACKWARDS;

    fun reversed(): Direction = when (this) {
        FORWARDS -> BACKWARDS
        BACKWARDS -> FORWARDS
    }
}