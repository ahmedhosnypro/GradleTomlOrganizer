package dependency

import net.swiftzer.semver.SemVer

data class VersionDeclaration(
    val name: String,
    val version: SemVer
) : DependencyNode() {
    var used: Boolean = false

    override fun toString(): String {
        return "$name = \"$version\""
    }
}