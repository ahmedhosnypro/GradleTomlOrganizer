import net.swiftzer.semver.SemVer
import org.junit.Assert.assertNotNull
import org.junit.Test
import parser.*
import resolver.ConflictResolver
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class DependencyParserTest {

    private val globalParser = DependencyParser()
    private val testInput = javaClass.getResource("/libs.version.toml")?.readText()!!

    // Version Declarations
    @Test
    fun `parseVersionDeclaration parses valid version declarations`() {
        val versionDecl = globalParser.parseVersionDeclaration("lib-module-version-ref= \"1.0.0\"")
        assert(versionDecl?.name == "lib-module-version-ref")
        assert(versionDecl?.version == SemVer.parse("1.0.0"))
    }

    @Test
    fun `parseVersionDeclaration handles missing version value`() {
        val versionDecl = globalParser.parseVersionDeclaration("libraryWithMissingVersion =")
        assert(versionDecl == null)
    }

    // Library Declarations
    @Test
    fun `parseLibraryDeclaration parses valid lib-module-version-ref declaration`() {
        val libDecl =
            parseLibraryModuleDeclaration("""lib-module-version-ref = { module = "group:name", version.ref = "lib-module-version-ref" }""")
        assert(libDecl?.name == "lib-module-version-ref")
        assert(libDecl?.module == "group:name")
        assert(libDecl?.versionRef == "lib-module-version-ref")
        assert(libDecl?.version == null)
        assert(libDecl?.group == null)
        assert(libDecl?.nameProp == null)
    }

    @Test
    fun `parseLibraryDeclaration parses valid lib-module-version declaration`() {
        val libDecl =
            parseLibraryModuleDeclaration("""lib-module-version = { module = "group:name", version = "1.2.3" }""")
        assert(libDecl?.name == "lib-module-version")
        assert(libDecl?.module == "group:name")
        assert(libDecl?.version == SemVer.parse("1.2.3"))
        assert(libDecl?.versionRef == null)
        assert(libDecl?.group == null)
        assert(libDecl?.nameProp == null)
    }

    @Test
    fun `parseLibraryDeclaration parses valid lib-group-name-version-ref declaration`() {
        val libDecl =
            parseLibraryGroupVersionDeclaration("""lib-group-name-version-ref = { group = "group", name = "name", version.ref = "lib-group-name-version-ref" }""")
        assert(libDecl?.name == "lib-group-name-version-ref")
        assert(libDecl?.group == "group")
        assert(libDecl?.nameProp == "name")
        assert(libDecl?.versionRef == "lib-group-name-version-ref")
        assert(libDecl?.module == null)
        assert(libDecl?.version == null)
    }

    @Test
    fun `parseLibraryDeclaration parses valid ib-group-name-version declaration`() {
        val libDecl =
            parseLibraryGroupVersionDeclaration("""ib-group-name-version = { group = "group", name = "name", version = "1.2.3" }""")
        assert(libDecl?.name == "ib-group-name-version")
        assert(libDecl?.group == "group")
        assert(libDecl?.nameProp == "name")
        assert(libDecl?.version == SemVer.parse("1.2.3"))
        assert(libDecl?.module == null)
        assert(libDecl?.versionRef == null)
    }

    // Invalid Library Declarations
    @Test
    fun `parseLibraryDeclaration handles invalid lib-version-invalid-no-module-or-group-name declaration`() {
        val libDecl =
            globalParser.parseLibraryDeclaration("""lib-version-invalid-no-module-or-group-name = { version = "0.0.1" }""")
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-module-invalid-no-version declaration`() {
        val libDecl =
            globalParser.parseLibraryDeclaration("""lib-module-invalid-no-version = { module = "group:name" }""")
        assert(libDecl == null)
    }


    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-missing-close-bracket declaration`() {
        val libDecl =
            globalParser.parseLibraryDeclaration("""lib-invalid-missing-close-bracket = { module = "group:name", version = "1.2.3" """)
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-use-brackets declaration`() {
        val libDecl =
            globalParser.parseLibraryDeclaration("""lib-invalid-use-brackets = (group = "group", name = "name", version = "1.2.3")""")
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-use-square-brackets declaration`() {
        val libDecl =
            globalParser.parseLibraryDeclaration("""lib-invalid-use-square-brackets = [group = "group", name = "name", version = "1.2.3"]""")
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-empty declaration`() {
        val libDecl = globalParser.parseLibraryDeclaration("""lib-invalid-empty = { }""")
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-line-one declaration`() {
        val libDecl = globalParser.parseLibraryDeclaration("""lib-invalid-line-one""")
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-line-two declaration`() {
        val libDecl = globalParser.parseLibraryDeclaration("""lib-invalid-line-two =""")
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-line-three declaration`() {
        val libDecl = globalParser.parseLibraryDeclaration("""lib-invalid-line-three = []""")
        assert(libDecl == null)
    }

    @Test
    fun `parseLibraryDeclaration handles invalid lib-invalid-line-four declaration`() {
        val libDecl = globalParser.parseLibraryDeclaration("""lib-invalid-line-four = ()""")
        assert(libDecl == null)
    }

    // Bundle Declarations
    @Test
    fun `parseBundleDeclaration parses valid bundle declarations`() {
        val validBundle =
            globalParser.parseBundleDeclaration("""Bundle = [ "lib-module-version-ref", "lib-module-version", "lib-group-name-version-ref", "ib-group-name-version" ]""")
        assert(validBundle?.name == "Bundle")
        assert(
            validBundle?.libraries == listOf(
                "lib-module-version-ref",
                "lib-module-version",
                "lib-group-name-version-ref",
                "ib-group-name-version"
            )
        )
    }

    @Test
    fun `parseBundleDeclaration parses valid multiline bundle declarations`() {
        val validBundle = globalParser.parseBundleDeclaration(
            """multilineBundle = [
            "lib-module-version-ref",
            "lib-module-version",
            "lib-group-name-version-ref",
            "ib-group-name-version",
        ]"""
        )
        assert(validBundle?.name == "multilineBundle")
        assert(
            validBundle?.libraries == listOf(
                "lib-module-version-ref",
                "lib-module-version",
                "lib-group-name-version-ref",
                "ib-group-name-version"
            )
        )
    }

    @Test
    fun `parseBundleDeclaration handles invalid bundle declarations`() {
        val invalidBundle =
            globalParser.parseBundleDeclaration("invalidBundle-missingClosingBracket = [// Missing closing bracket")
        assert(invalidBundle == null)
    }

    // ... other imports and code ...


    // Valid Plugin Declarations
    @Test
    fun `parsePluginDeclaration parses valid plugin-id-version-ref declaration`() {
        val pluginDecl =
            globalParser.parsePluginDeclaration("""plugin-id-version-ref = { id = "plugin.id", version.ref = "plugin-id-version-ref" }""")
        assert(pluginDecl?.name == "plugin-id-version-ref")
        assert(pluginDecl?.id == "plugin.id")
        assert(pluginDecl?.versionRef == "plugin-id-version-ref")
        assert(pluginDecl?.version == null)
    }

    @Test
    fun `parsePluginDeclaration parses valid plugin-id-version declaration`() {
        val pluginDecl =
            globalParser.parsePluginDeclaration("""plugin-id-version = { id = "plugin.id", version = "0.1.0" }""")
        assert(pluginDecl?.name == "plugin-id-version")
        assert(pluginDecl?.id == "plugin.id")
        assert(pluginDecl?.version == SemVer.parse("0.1.0"))
        assert(pluginDecl?.versionRef == null)
    }

    // Invalid Plugin Declarations
    @Test
    fun `parsePluginDeclaration handles invalid plugin-id-invalid-no-version declaration`() {
        val pluginDecl = globalParser.parsePluginDeclaration("""plugin-id-invalid-no-version = { id = "plugin.id" }""")
        assert(pluginDecl == null) // Or handle error as needed
    }

    @Test
    fun `parsePluginDeclaration handles invalid plugin-version-invalid-no-id declaration`() {
        val pluginDecl = globalParser.parsePluginDeclaration("""plugin-version-invalid-no-id = { version = "0.0.1" }""")
        assert(pluginDecl == null)
    }

    @Test
    fun `parsePluginDeclaration handles invalid plugin-invalid-empty declaration`() {
        val pluginDecl = globalParser.parsePluginDeclaration("""plugin-invalid-empty = { }""")
        assert(pluginDecl == null)
    }

    @Test
    fun `parse comment`() {
        val comment = globalParser.parseComment("# This is a comment")
        assert(comment?.content == "This is a comment")
    }

    @Test
    fun testParseDependencies() {
        assertNotNull(testInput)

        val parser = DependencyParser()
        parser.parseDependencies(testInput)
        // Assertions for Version Declarations
        val versionRefs = parser.versionRefs
        assert(versionRefs.size == 4)

        assert(versionRefs.find { it.name == "lib-module-version-ref" }?.version == SemVer.parse("1.0.0"))
        assert(versionRefs.find { it.name == "libraryWithDirectVersion" }?.version == SemVer.parse("2.5.4-beta"))
        assert(versionRefs.find { it.name == "lib-group-name-version-ref" }?.version == SemVer.parse("1.2.3"))
        assert(versionRefs.find { it.name == "plugin-id-version-ref" }?.version == SemVer.parse("0.1.0"))

        // Assertions for Library Declarations
        val libraries = parser.libraries
        assert(libraries.size == 5) // 5 valid library declarations

        val libModuleVersionRef = libraries.find { it.name == "lib-module-version-ref" }
        assert(libModuleVersionRef?.module == "group.one:name.one")
        assert(libModuleVersionRef?.versionRef == "lib-module-version-ref")
        assert(libModuleVersionRef?.version == null)
        assert(libModuleVersionRef?.group == null)
        assert(libModuleVersionRef?.nameProp == null)

        val libModuleVersion = libraries.find { it.name == "lib-module-version" }
        assert(libModuleVersion?.module == "group.one:name.one")
        assert(libModuleVersion?.version == SemVer.parse("1.2.3"))
        assert(libModuleVersion?.versionRef == null)
        assert(libModuleVersion?.group == null)
        assert(libModuleVersion?.nameProp == null)

        val libGroupNameVersionRef = libraries.find { it.name == "lib-group-name-version-ref" }
        assert(libGroupNameVersionRef?.group == "group.two")
        assert(libGroupNameVersionRef?.nameProp == "name.two")
        assert(libGroupNameVersionRef?.versionRef == "lib-group-name-version-ref")
        assert(libGroupNameVersionRef?.module == null)
        assert(libGroupNameVersionRef?.version == null)

        val libGroupNameVersion = libraries.find { it.name == "lib-group-name-version" }
        assert(libGroupNameVersion?.group == "group.two")
        assert(libGroupNameVersion?.nameProp == "name.two")
        assert(libGroupNameVersion?.version == SemVer.parse("1.2.3"))
        assert(libGroupNameVersion?.module == null)
        assert(libGroupNameVersion?.versionRef == null)

        val libGroupName = libraries.find { it.name == "lib-group-name" }
        assert(libGroupName?.group == "group")
        assert(libGroupName?.nameProp == "name")
        assert(libGroupName?.version == null)
        assert(libGroupName?.module == null)
        assert(libGroupName?.versionRef == null)

        // Assertions for Bundle Declarations
        val bundles = parser.bundles
        assert(bundles.size == 2) // 2 valid bundle declarations

        val bundleDependencyList = listOf(
            "lib-module-version-ref",
            "lib-module-version",
            "lib-group-name-version-ref",
            "ib-group-name-version"
        )
        val bundle1 = bundles.find { it.name == "Bundle" }
        assert(bundle1?.libraries?.size == 4)
        bundle1?.libraries?.containsAll(bundleDependencyList)?.let { assert(it) }


        val bundle2 = bundles.find { it.name == "multilineBundle" }
        assert(bundle2?.libraries?.size == 4)
        bundle2?.libraries?.containsAll(bundleDependencyList)?.let { assert(it) }

        // Assertions for Plugin Declarations
        val plugins = parser.plugins
        assert(plugins.size == 2) // 2 valid plugin declarations


        val plugin1 = plugins.find { it.name == "plugin-id-version-ref" }
        assert(plugin1?.id == "plugin.id")
        assert(plugin1?.versionRef == "plugin-id-version-ref")
        assert(plugin1?.version == null)

        val plugin2 = plugins.find { it.name == "plugin-id-version" }
        assert(plugin2?.id == "plugin.id")
        assert(plugin2?.version == SemVer.parse("0.1.0"))
        assert(plugin2?.versionRef == null)

        // Additional Assertions for Error Handling
        // (Assuming you are printing warnings using println)

        // Capture standard output (println output)
//        val output = captureOutput {
//            parser.parseDependencies(testInput)
//        }

        // Assert that specific warnings are present in the output
//        assert(output.contains("Warning: Unrecognized line format: libraryWithMissingVersion ="))
//        assert(output.contains("Warning: Unrecognized line format: lib-invalid-missing-close-bracket = { module = \"group:name\", version = \"1.2.3\""))
        // ... (Add assertions for other expected warning messages) ...

        // Additional Assertions for Error Handling
        // You can add assertions to check if any warnings or errors were logged
        // during the parsing process, based on how you handle errors in
        // your parser.DependencyParser class.
    }

    @Test
    fun testLibraryVersionResolver() {
        val parser = DependencyParser()
        parser.parseDependencies(testInput)
        val resolver = ConflictResolver(parser.dependencyNodes)
        assert(resolver.resolvedVersions.size == 4)
        assert(resolver.resolvedLibraries.size == 3)
        assert(resolver.resolvedBundles.size == 2)
        assert(resolver.resolvedPlugins.size == 1)

    }

    // Helper function to capture standard output
    private fun captureOutput(block: () -> Unit): String {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        block.invoke() // Execute the code that prints to the console

        System.setOut(originalOut) // Restore original output stream
        return outputStream.toString()
    }
}