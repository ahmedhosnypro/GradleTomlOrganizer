import resolver.ConflictResolver
import java.io.File


const val inputFilePath = "/home/lt/IdeaProjects/CatalogOrganizer/libs.versions.toml"
const val outputFilePath = "/home/lt/IdeaProjects/CatalogOrganizer/output/libs.versions.toml"


fun main(args: Array<String>) {
//    val cliHandler = CliHandler()
//    val cliOptions = cliHandler.parseArguments(args)

    val parser = parser.DependencyParser()
//    parser.parseFileDependencies(cliOptions.inputFile)
    parser.parseFileDependencies(inputFilePath)
    val resolver = ConflictResolver(parser.dependencyNodes)

//    val optimizer = DependencyOptimizer(parser, resolver, outputGenerator
//    val optimizedOutput = optimizer.optimizeDependencies(cliOptions.inputFile)

//    val outputFile = File(cliOptions.outputFile ?: "")
    val outputFile = File(outputFilePath)

    val output = OutputGenerator.generateOutput(resolver)
    if (outputFile.absolutePath.isNotEmpty()) {
        outputFile.parentFile.mkdirs() // Ensure the output directory exists
        outputFile.writeText(output)
        println("Optimized dependencies written to: ${outputFile.absolutePath}")
    } else {
        println(output)
    }
}

