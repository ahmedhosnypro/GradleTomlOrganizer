package dependency

import net.swiftzer.semver.SemVer

data class LibraryDeclaration(
    val name: String? = null,
    val module: String? = null,
    val versionRef: String? = null,
    var version: SemVer? = null,
    var group: String? = null,
    var nameProp: String? = null
) : DependencyNode() {
    override fun toString(): String {
        val isModule = module != null
        val isVersionRef = versionRef != null
        val sb = StringBuilder()
        sb.append(name).append(" = {")
        if (isModule) {
            sb.append(" module = \"$module\"")
        } else {
            sb.append(" group = \"$group\", name = \"$nameProp\"")
        }
        if (isVersionRef) {
            sb.append(", versionRef = \"$versionRef\"")
        } else {
            sb.append(", version = \"$version\"")
        }

        sb.append("}")
        return sb.toString()
    }
}