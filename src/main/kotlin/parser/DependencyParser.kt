package parser

import BundleDeclaration
import CommentNode
import DependencyNode
import LibraryDeclaration
import PluginDeclaration
import VersionDeclaration
import java.io.File

class DependencyParser(
    val strictMode: Boolean = false,
) {
    val dependencyNodes = mutableListOf<DependencyNode>()
    private lateinit var dependencySections: DependencySections

    val versionRefs: List<VersionDeclaration>
        get() = dependencyNodes.filterIsInstance<VersionDeclaration>()

    val libraries: List<LibraryDeclaration>
        get() = dependencyNodes.filterIsInstance<LibraryDeclaration>()

    val bundles: List<BundleDeclaration>
        get() = dependencyNodes.filterIsInstance<BundleDeclaration>()

    val plugins: List<PluginDeclaration>
        get() = dependencyNodes.filterIsInstance<PluginDeclaration>()

    fun parseFileDependencies(inputFilePath: String): List<DependencyNode> {
        val file = File(inputFilePath)
        if (!file.exists()) {
            println("Error: Input file not found: $inputFilePath")
            return emptyList()
        }

        val input = file.readText()
        return parseDependencies(input)
    }

    fun parseDependencies(input: String): List<DependencyNode> {
        dependencySections = parseSections(input)


        dependencyNodes.addAll(dependencySections.versions.mapNotNull { parseVersionDeclaration(it) })
        dependencyNodes.addAll(dependencySections.libraries.mapNotNull { parseLibraryDeclaration(it) })
        dependencyNodes.addAll(dependencySections.bundles.mapNotNull { parseBundleDeclaration(it) })
        dependencyNodes.addAll(dependencySections.plugins.mapNotNull { parsePluginDeclaration(it) })

        // Handle unknowns (log a warning or throw an exception if needed)
        dependencySections.unknowns.forEach { (sectionName, line) ->
            println("Warning: Unknown section '$sectionName' with line: $line")
        }

        return dependencyNodes
    }


    fun parseComment(line: String): CommentNode? {
        return if (line.trim().startsWith("#")) {
            CommentNode(line.trim().removePrefix("#").trim())
        } else null
    }
}