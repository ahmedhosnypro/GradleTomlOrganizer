package parser

data class DependencySections(
    val versions: MutableList<String> = mutableListOf(),
    val libraries: MutableList<String> = mutableListOf(),
    val bundles: MutableList<String> = mutableListOf(),
    val plugins: MutableList<String> = mutableListOf(),
    val unknowns: MutableList<Pair<String, String>> = mutableListOf()
)