package resolver

import VersionDeclaration

fun VersionResolver.resolveVersionRefVersions(): List<VersionDeclaration> {
    versionRefs.groupBy { it.name }.forEach { (_, versions) ->
        when (versionResolutionStrategy) {
            VersionResolutionStrategy.HIGHEST_VERSION -> resolveHighestVersion(versions)
            VersionResolutionStrategy.LOWEST_VERSION -> resolveLowestVersion(versions)
        }
    }

    return resolvedVersions
}

private fun VersionResolver.resolveHighestVersion(versions: List<VersionDeclaration>) {
    val resolvedVersion = versions.maxBy { it.version }
    resolvedVersions.add(resolvedVersion)
}

private fun VersionResolver.resolveLowestVersion(versions: List<VersionDeclaration>) {
    val resolvedVersion = versions.minBy { it.version }
    resolvedVersions.add(resolvedVersion)
}