package resolver

import PluginDeclaration

fun ConflictResolver.resolvePluginVersions(): List<PluginDeclaration> {
    plugins.forEach { plugin ->
        if (plugin.versionRef != null && plugin.version == null) {
            val versionRef = versionRefs.find { version ->
                version.name == plugin.versionRef
            }
            versionRef?.used = true
            plugin.version = versionRef?.version
        }
    }

    plugins.groupBy { it.id }.forEach { (_, plugins) ->
        resolvePlugins(plugins)
    }

    return resolvedPlugins
}

private fun ConflictResolver.resolvePlugins(plugins: List<PluginDeclaration>) {
    when (versionResolutionStrategy) {
        VersionResolutionStrategy.HIGHEST_VERSION -> resolveHighestVersion(plugins)
        VersionResolutionStrategy.LOWEST_VERSION -> resolveLowestVersion(plugins)
    }
}

private fun ConflictResolver.resolveHighestVersion(plugins: List<PluginDeclaration>) {
    if (plugins.any { it.version != null }) {
        val resolvedPlugin = plugins.maxByOrNull { it.version!! }
        resolvedPlugins.add(resolvedPlugin!!)
    } else {
        resolvePluginVersion(plugins)
    }
}

private fun ConflictResolver.resolveLowestVersion(plugins: List<PluginDeclaration>) {
    if (plugins.any { it.version != null }) {
        val resolvedPlugin = plugins.minByOrNull { it.version!! }
        resolvedPlugins.add(resolvedPlugin!!)
    } else {
        resolvePluginVersion(plugins)
    }
}

private fun ConflictResolver.resolvePluginVersion(plugins: List<PluginDeclaration>) {
    val plugin = plugins[0]
    resolvedPlugins.add(
        PluginDeclaration(
            name = plugin.name,
            id = plugin.id,
            version = plugin.version,
            versionRef = plugin.versionRef
        )
    )
}