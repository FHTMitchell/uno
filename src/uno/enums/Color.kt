package uno.enums

enum class Color {
    RED, BLUE, GREEN, YELLOW;

    companion object {
        fun randomColor() = values().random()
    }

}