package parser

enum class SectionType(val value: String) {
    VERSIONS("[versions]"),
    LIBRARIES("[libraries]"),
    BUNDLES("[bundles]"),
    PLUGINS("[plugins]"),
    UNKNOWN("[..]")
}

fun parseSections(input: String): DependencySections {
    val dependencySections = DependencySections()
    var currentSectionType: SectionType = SectionType.UNKNOWN
    val currentDeclarationBuilder = StringBuilder()

    input.lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .forEach { line ->
            if (isSectionHeader(line)) {
                currentSectionType = currentSection(line)
                currentDeclarationBuilder.clear()
            } else handleSectionLine(dependencySections, currentDeclarationBuilder, line, currentSectionType)
        }

    addDeclarationToSection(dependencySections, currentDeclarationBuilder, currentSectionType)

    return dependencySections
}

private fun isSectionHeader(line: String): Boolean = line.startsWith("[") && line.endsWith("]")

private fun currentSection(line: String): SectionType = when (line) {
    SectionType.VERSIONS.value -> SectionType.VERSIONS
    SectionType.LIBRARIES.value -> SectionType.LIBRARIES
    SectionType.BUNDLES.value -> SectionType.BUNDLES
    SectionType.PLUGINS.value -> SectionType.PLUGINS
    else -> SectionType.UNKNOWN
}

private fun handleSectionLine(
    dependencySections: DependencySections,
    currentDeclarationBuilder: StringBuilder,
    line: String,
    currentSectionType: SectionType
) {
    if (currentDeclarationBuilder.isNotEmpty() && line.isNotEmpty()) {
        currentDeclarationBuilder.append(" ") // Add space for continuation
    }
    currentDeclarationBuilder.append(line)

    when (currentSectionType) {
        SectionType.VERSIONS -> handleVersionsSectionLine(dependencySections, currentDeclarationBuilder, line)
        SectionType.LIBRARIES -> handleLibraryOrPluginSectionLine(
            dependencySections,
            currentDeclarationBuilder,
            line,
            SectionType.LIBRARIES
        )

        SectionType.BUNDLES -> handleBundlesSectionLine(dependencySections, currentDeclarationBuilder, line)
        SectionType.PLUGINS -> handleLibraryOrPluginSectionLine(
            dependencySections,
            currentDeclarationBuilder,
            line,
            SectionType.PLUGINS
        )
        else -> Unit
    }
}

private fun handleVersionsSectionLine(
    dependencySections: DependencySections,
    currentDeclarationBuilder: StringBuilder,
    line: String
) {
    if (line.contains("=") && !line.endsWith("{") && !line.endsWith("[")) {
        addDeclarationToSection(currentDeclarationBuilder, dependencySections.versions)
        currentDeclarationBuilder.clear()
    }
}

private fun handleLibraryOrPluginSectionLine(
    dependencySections: DependencySections,
    currentDeclarationBuilder: StringBuilder,
    line: String,
    sectionType: SectionType
) {
    if (line.endsWith("}")) {
        addDeclarationToSection(dependencySections, currentDeclarationBuilder, sectionType)
        currentDeclarationBuilder.clear()
    }
}

private fun handleBundlesSectionLine(
    dependencySections: DependencySections,
    currentDeclarationBuilder: StringBuilder,
    line: String
) {
    if (line.endsWith("]")) {
        addDeclarationToSection(currentDeclarationBuilder, dependencySections.bundles)
        currentDeclarationBuilder.clear()
    }
}

private fun addDeclarationToSection(
    currentDeclarationBuilder: StringBuilder,
    currentSection: MutableList<String>?
) {
    if (currentDeclarationBuilder.isNotBlank() && currentSection != null) {
        currentSection.add(currentDeclarationBuilder.toString().trim())
    }
}

private fun addDeclarationToSection(
    dependencySections: DependencySections,
    currentDeclarationBuilder: StringBuilder,
    currentSectionType: SectionType
) {
    when (currentSectionType) {
        SectionType.VERSIONS -> addDeclarationToSection(currentDeclarationBuilder, dependencySections.versions)
        SectionType.LIBRARIES -> addDeclarationToSection(currentDeclarationBuilder, dependencySections.libraries)
        SectionType.BUNDLES -> addDeclarationToSection(currentDeclarationBuilder, dependencySections.bundles)
        SectionType.PLUGINS -> addDeclarationToSection(currentDeclarationBuilder, dependencySections.plugins)
        else -> Unit
    }
}
