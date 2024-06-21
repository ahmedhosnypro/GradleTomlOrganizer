package resolver

import LibraryDeclaration

fun VersionResolver.resolveLibraryVersions(): List<LibraryDeclaration> {
    libraries.forEach { library ->
        if (library.versionRef != null && library.version == null) {
            val versionRef = versionRefs.find { version ->
                version.name == library.versionRef
            }
            versionRef?.used = true
            library.version = versionRef?.version
        }

        if (library.module != null && library.module.contains(":")) {
            val (group, nameProp) = library.module.split(":")
            library.group = group
            library.nameProp = nameProp
        }

    }

    libraries.groupBy { it.group to it.nameProp }.forEach { (_, libraries) ->
        resolveLibraries(libraries)
    }

    return resolvedLibraries
}

private fun VersionResolver.resolveLibraries(libraries: List<LibraryDeclaration>) {
    when (versionResolutionStrategy) {
        VersionResolutionStrategy.HIGHEST_VERSION -> resolveHighestVersion(libraries)
        VersionResolutionStrategy.LOWEST_VERSION -> resolveLowestVersion(libraries)
    }
}

private fun VersionResolver.resolveHighestVersion(libraries: List<LibraryDeclaration>) {
    if (libraries.any { it.version != null }) {
        val resolvedLibrary = libraries.maxByOrNull { it.version!! }
        resolvedLibraries.add(resolvedLibrary!!)
    } else {
        resolveLibVersion(libraries)
    }
}

private fun VersionResolver.resolveLowestVersion(libraries: List<LibraryDeclaration>): List<LibraryDeclaration> {
    if (libraries.any { it.version != null }) {
        val resolvedLibrary = libraries.minByOrNull { it.version!! }
        resolvedLibraries.add(resolvedLibrary!!)
    } else {
        resolveLibVersion(libraries)
    }
    return resolvedLibraries
}

private fun VersionResolver.resolveLibVersion(libraries: List<LibraryDeclaration>) {
    val lib = libraries.firstOrNull { it.module != null && it.versionRef != null }
        ?: libraries.firstOrNull { it.group != null && it.name != null && it.versionRef != null }
        ?: libraries.firstOrNull { it.group != null && it.name != null }
        ?: libraries[0]
    resolvedLibraries.add(
        LibraryDeclaration(
            name = lib.name,
            group = lib.group,
            nameProp = lib.nameProp,
            version = lib.version,
            versionRef = lib.versionRef
        )
    )
}