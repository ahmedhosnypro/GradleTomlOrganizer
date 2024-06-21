import net.swiftzer.semver.SemVer

sealed class DependencyNode
data class VersionDeclaration(
    val name: String,
    val version: SemVer,
    val used: Boolean = false
) : DependencyNode()

data class LibraryDeclaration(
    var name: String? = null,
    val module: String? = null,
    val versionRef: String? = null,
    var version: SemVer? = null,
    var group: String? = null,
    val nameProp: String? = null
) : DependencyNode()

data class BundleDeclaration(val name: String, val dependencies: List<String>) : DependencyNode()
data class PluginDeclaration(
    val name: String,
    val id: String,
    val versionRef: String? = null,
    val version: SemVer? = null
) : DependencyNode()

data class CommentNode(val content: String) : DependencyNode()