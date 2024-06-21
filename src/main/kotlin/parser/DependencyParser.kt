package parser

import CommentNode
import DependencyNode
import java.io.File

class DependencyParser(
    val strictMode: Boolean = false,
) {
    private lateinit var dependencySections: DependencySections

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

        val dependencyNodes = mutableListOf<DependencyNode>()
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