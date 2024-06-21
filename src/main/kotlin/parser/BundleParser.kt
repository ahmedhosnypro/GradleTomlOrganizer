package parser

import BundleDeclaration

fun DependencyParser.parseBundleDeclaration(bundleDeclaration: String): BundleDeclaration? {
    return parseBundleDeclarationOrNull(bundleDeclaration) ?: if (strictMode) {
        throw InvalidBundleDeclarationError("Invalid bundle declaration: $bundleDeclaration")
    } else {
        println("Warning: Invalid bundle declaration: $bundleDeclaration")
        null
    }
}

fun parseBundleDeclarationOrNull(bundleDeclaration: String): BundleDeclaration? {
    val matchResult = RegexPatterns.bundleDeclaration.find(bundleDeclaration)
    return if (matchResult != null) {
        try {
            val (name, dependenciesStr) = matchResult.destructured
            val dependencies = dependenciesStr
                .lines() // Split into lines
                .flatMap { s ->
                    s.split(",") // Split each s by comma
                }
                .map { it.trim().removeSurrounding("\"") } // Trim and remove quotes
                .filter { it.isNotBlank() } // Remove empty entries
            BundleDeclaration(name.trim(), dependencies)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    } else null
}
