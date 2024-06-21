package parser

import PluginDeclaration
import net.swiftzer.semver.SemVer

fun DependencyParser.parsePluginDeclaration(pluginDeclaration: String): PluginDeclaration? {
    return parsePluginDeclarationOrNull(pluginDeclaration)
        ?: if (strictMode) {
            throw InvalidPluginDeclarationError("Invalid plugin declaration: $pluginDeclaration")
        } else {
            println("Warning: Invalid plugin declaration: $pluginDeclaration. Skipping...")
            null
        }
}


private fun parsePluginDeclarationOrNull(pluginDeclaration: String): PluginDeclaration? {
    var matchResult = RegexPatterns.pluginDeclarationWithVersionRef.find(pluginDeclaration)
    val isVersionReg = if (matchResult != null) true else {
        matchResult = RegexPatterns.pluginDeclarationWithVersion.find(pluginDeclaration)
        false
    }

    return if (matchResult != null) {
        try {
            val (name, id, version) = matchResult.destructured
            PluginDeclaration(
                name = name.removeSurrounding("\""),
                id = id.removeSurrounding("\""),
                version = if (isVersionReg) null else SemVer.parse(version.removeSurrounding("\"")),
                versionRef = if (isVersionReg) version.removeSurrounding("\"") else null
            )
        } catch (e: Exception) {
            println(e.message)
            null
        }
    } else null
}
