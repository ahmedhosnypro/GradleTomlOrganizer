package dependency

import net.swiftzer.semver.SemVer

data class PluginDeclaration(
    val name: String,
    val id: String,
    val versionRef: String? = null,
    var version: SemVer? = null
) : DependencyNode() {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(name).append(" = {")
        sb.append(" id = \"$id\"")
        if (versionRef != null) {
            sb.append(", versionRef = \"$versionRef\"")
        } else {
            sb.append(", version = \"$version\"")
        }
        sb.append("}")
        return sb.toString()
    }
}