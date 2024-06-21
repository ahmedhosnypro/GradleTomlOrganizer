package parser

import VersionDeclaration
import net.swiftzer.semver.SemVer

fun DependencyParser.parseVersionDeclaration(versionDeclaration: String): VersionDeclaration? {
    return parseVersionDeclarationOrNull(versionDeclaration) ?: if (strictMode) {
        throw InvalidVersionDeclarationError("Invalid version declaration: $versionDeclaration")
    } else {
        println("Warning: Invalid version declaration: $versionDeclaration. Skipping...")
        null
    }
}

// Individual parsing functions for each declaration type
private fun parseVersionDeclarationOrNull(versionDeclaration: String): VersionDeclaration? {
    val matchResult = RegexPatterns.versionDeclaration.find(versionDeclaration)
    return if (matchResult != null) {
        try {
            val (name, version) = matchResult.destructured
            val semVer = SemVer.parse(version.trim().removeSurrounding("\""))
            VersionDeclaration(name.trim(), semVer)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    } else null
}