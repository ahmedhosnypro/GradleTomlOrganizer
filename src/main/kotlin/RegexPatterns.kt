object RegexPatterns {
    // Version declarations
    val versionDeclaration = Regex("""(\S+)\s*=\s*"([^"]*)"""")

    // Library declarations
    val libraryWithModuleAndVersionRef =
        Regex("""(\S+)\s*=\s*\{\s*module\s*=\s*"([^"]*)",\s*version\.ref\s*=\s*"([^"]*)"\s*}""")
    val libraryWithModuleAndVersion =
        Regex("""(\S+)\s*=\s*\{\s*module\s*=\s*"([^"]*)",\s*version\s*=\s*"([^"]*)"\s*}""")
    val libraryWithGroupAndNameAndVersionRef =
        Regex("""(\S+)\s*=\s*\{\s*group\s*=\s*"([^"]*)",\s*name\s*=\s*"([^"]*)",\s*version\.ref\s*=\s*"([^"]*)"\s*}""")
    val libraryWithGroupAndNameAndVersion =
        Regex("""(\S+)\s*=\s*\{\s*group\s*=\s*"([^"]*)",\s*name\s*=\s*"([^"]*)",\s*version\s*=\s*"([^"]*)"\s*}""")
    val libraryWithGroupAndName =
        Regex("""(\S+)\s*=\s*\{\s*group\s*=\s*"([^"]*)",\s*name\s*=\s*"([^"]*)"\s*}""")

    // Bundle declaration
    val bundleDeclaration = Regex("""(\S+)\s*=\s*\[([\s\S]*?)]""", RegexOption.DOT_MATCHES_ALL)

    // Plugin declarations
    val pluginDeclarationWithVersionRef =
        Regex("""(\S+)\s*=\s*\{\s*id\s*=\s*"([^"]*)",\s*version\.ref\s*=\s*"([^"]*)"\s*}""")
    val pluginDeclarationWithVersion =
        Regex("""(\S+)\s*=\s*\{\s*id\s*=\s*"([^"]*)",\s*version\s*=\s*"([^"]*)"\s*}""")

    // Comments and section headers
    val comment = Regex("""#.*""")
    val sectionHeader = Regex("""\[(\w+)]""")

    // Invalid declarations
    val invalidDeclaration = Regex("""(\S+)\s*=\s*\{([^}]*)}""")
    val undefinedLine = Regex("""^(?!\s*#|\s*\[).*$""")
}
