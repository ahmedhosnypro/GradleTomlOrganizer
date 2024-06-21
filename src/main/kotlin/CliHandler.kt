import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required

import java.io.File

data class CliOptions(
    val inputFile: String = "",
    val outputFile: String? = null,
    val verbosity: Int = 0,
    val strictMode: Boolean = false,
    val strategy: String = "default"
)

class CliHandler {
    fun parseArguments(args: Array<String>): CliOptions {
        val parser = ArgParser("dependency-optimizer")

        val inputFile by parser.option(ArgType.String, shortName = "i", description = "Path to the input dependency file")
            .required()
        val outputFile by parser.option(ArgType.String, shortName = "o", description = "Path to the output file (default: same directory as input file under 'output' subdirectory)")
        val verbose by parser.option(ArgType.Int, shortName = "v", description = "Verbosity level (0-3)")
            .default(0)
        val strict by parser.option(ArgType.Boolean, shortName = "s", description = "Enable strict mode")
            .default(false)
        val strategy by parser.option(ArgType.String, shortName = "st", description = "Conflict resolution strategy (default: 'default')")
            .default("default")

        parser.parse(args)

        // Set default output file path
        val defaultOutputFile = if (inputFile.isNotEmpty()) {
            val inputFileObj = File(inputFile)
            val outputDir = File(inputFileObj.parentFile, "output")
            File(outputDir, inputFileObj.name).absolutePath
        } else {
            null
        }

        return CliOptions(
            inputFile = inputFile,
            outputFile = outputFile ?: defaultOutputFile, // Use default if not provided
            verbosity = verbose,
            strictMode = strict,
            strategy = strategy
        )
    }
}