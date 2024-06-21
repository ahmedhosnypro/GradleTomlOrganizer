import resolver.ConflictResolver

object OutputGenerator {
    fun generateOutput(resolver: ConflictResolver): String {
        val sb = StringBuilder()

        sb.append("[versions]\n")
        resolver.resolvedVersions.sortBy { it.name }
        resolver.resolvedVersions.filter { it.used }.forEach {
            sb.append(it.toString()).append("\n")
        }

        sb.append("\n\n[libraries]\n")
        resolver.resolvedLibraries.sortBy { it.name }
        resolver.resolvedLibraries.forEach {
            sb.append(it.toString()).append("\n")
        }

        sb.append("\n\n[bundles]\n")
        resolver.resolvedBundles.sortBy { it.name }
        resolver.resolvedBundles.forEach {
            sb.append(it.toString()).append("\n")
        }

        sb.append("\n\n[plugins]\n")
        resolver.resolvedPlugins.sortBy { it.name }
        resolver.resolvedPlugins.forEach {
            sb.append(it.toString()).append("\n")
        }

        return sb.toString()
    }
}