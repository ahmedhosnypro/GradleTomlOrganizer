# libs.versions.toml Optimizer

Kotlin script for optimizing Gradle-style dependency files, designed for maintainability and version conflict
resolution.

## Features

- **Intelligent Parsing:** Accurately parses various Gradle dependency declaration styles,
  including `version.ref`, `module`, `group`, `name`, and direct `version` specifications.

- **AST-Based Representation:** Constructs an Abstract Syntax Tree (AST) to represent dependencies, preserving comments
  and original formatting.

- **Version Conflict Resolution:** Implements multiple strategies for resolving version conflicts, ensuring the highest
  compatible dependencies are used.

- **Customizable Output:**  Supports outputting the optimized dependency file to the console or a specified file path.

- **Error Handling:** Provides detailed error messages and recovery mechanisms for invalid input.

- **Extensible Architecture:** Designed for extensibility, allowing for future support of additional dependency file
  formats and custom optimization logic.

### Running

```bash
./gradlew run --args="--input <path-to-dependency-file> [options]"
```

**Options:**

- `-i`, `--input <file_path>`: **(Required)** Path to the input dependency file.
- `-o`, `--output <file_path>`: Path to the output file (default: creates an "output" subdirectory in the same location
  as the input file).
- `-v`, `--verbosity <level>`: Verbosity level (0-3).
- `-s`, `--strict`: Enable strict mode for error handling.
- `-st`, `--strategy <strategy_name>`: Custom conflict resolution strategy.

## Examples

```bash
# Optimize dependencies
./gradlew run --args="--input libs.versions.toml -o output/libs.versions.toml" 
```

## License

This project is licensed under the [MIT License](LICENSE). 
