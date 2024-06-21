package parser

import dependency.LibraryDeclaration
import net.swiftzer.semver.SemVer

fun DependencyParser.parseLibraryDeclaration(libraryDeclaration: String): LibraryDeclaration? {
    return parseLibraryModuleDeclaration(libraryDeclaration)
        ?: parseLibraryGroupDeclaration(libraryDeclaration)
        ?: parseLibraryGroupVersionDeclaration(libraryDeclaration)
        ?: if (strictMode) {
            throw InvalidLibraryDeclarationError("Invalid library declaration: $libraryDeclaration")
        } else {
            println("Warning: Invalid library declaration: $libraryDeclaration. Skipping...")
            null
        }
}

fun parseLibraryModuleDeclaration(libraryDeclaration: String): LibraryDeclaration? {
    var matchResult = RegexPatterns.libraryWithModuleAndVersionRef.find(libraryDeclaration)
    val isVersionRef = if (matchResult != null) true else {
        matchResult = RegexPatterns.libraryWithModuleAndVersion.find(libraryDeclaration)
        false
    }
    return if (matchResult != null) {
        try {
            val (name, module, version) = matchResult.destructured
            LibraryDeclaration(
                name = name.removeSurrounding("\""),
                module = module.removeSurrounding("\""),
                versionRef = if (isVersionRef) version.removeSurrounding("\"") else null,
                version = if (!isVersionRef) SemVer.parse(version.removeSurrounding("\"")) else null
            )
        } catch (e: Exception) {
            println(e.message)
            null
        }
    } else null
}

private fun parseLibraryGroupDeclaration(libraryDeclaration: String): LibraryDeclaration? {
    val matchResult = RegexPatterns.libraryWithGroupAndName.find(libraryDeclaration)

    return if (matchResult != null) {
        try {
            val (name, group, nameProp) = matchResult.destructured
            LibraryDeclaration(
                name = name.removeSurrounding("\""),
                group = group.removeSurrounding("\""),
                nameProp = nameProp.removeSurrounding("\"")
            )
        } catch (e: Exception) {
            println(e.message)
            null
        }
    } else null
}

fun parseLibraryGroupVersionDeclaration(libraryDeclaration: String): LibraryDeclaration? {
    var matchResult = RegexPatterns.libraryWithGroupAndNameAndVersionRef.find(libraryDeclaration)
    val isVersionRef = if (matchResult != null) true else {
        matchResult = RegexPatterns.libraryWithGroupAndNameAndVersion.find(libraryDeclaration)
        false
    }

    return if (matchResult != null) {
        try {
            val (name, group, nameProp, version) = matchResult.destructured
            LibraryDeclaration(
                name = name.removeSurrounding("\""),
                group = group.removeSurrounding("\""),
                nameProp = nameProp.removeSurrounding("\""),
                version = if (isVersionRef) null else SemVer.parse(version.removeSurrounding("\"")),
                versionRef = if (isVersionRef) version.removeSurrounding("\"") else null
            )
        } catch (e: Exception) {
            println(e.message)
            null
        }
    } else null
}
