import com.catalog.DependencyOptimizer
import java.io.File


const val input = "/home/lt/IdeaProjects/CatalogOrganizer/libs.versions.toml"
const val output = "/home/lt/IdeaProjects/CatalogOrganizer/output/libs.versions.toml"



fun main(args: Array<String>) {
    val cliHandler = CliHandler()
    val cliOptions = cliHandler.parseArguments(args)

    val parser = parser.DependencyParser()
    val resolver = VersionResolver()
    val outputGenerator = OutputGenerator()
    val optimizer = DependencyOptimizer(parser, resolver, outputGenerator)

    val optimizedOutput = optimizer.optimizeDependencies(cliOptions.inputFile)

    val outputFile = File(cliOptions.outputFile ?: "")

    if (outputFile.absolutePath.isNotEmpty()) {
        outputFile.parentFile.mkdirs() // Ensure the output directory exists
        outputFile.writeText(optimizedOutput)
        println("Optimized dependencies written to: ${outputFile.absolutePath}")
    } else {
        println(optimizedOutput)
    }
}

