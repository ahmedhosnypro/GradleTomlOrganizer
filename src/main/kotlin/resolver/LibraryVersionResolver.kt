package resolver

import LibraryDeclaration
import VersionDeclaration

class LibraryVersionResolver(
    private val libraries: List<LibraryDeclaration>,
    private val versionRefs: List<VersionDeclaration>,
    private val versionResolutionStrategy: VersionResolutionStrategy = VersionResolutionStrategy.HIGHEST_VERSION,
    private val versionResolutionChannel: VersionResolutionChannel = VersionResolutionChannel.RELEASE
) {
    private val resolvedLibraries = mutableListOf<LibraryDeclaration>()

    fun resolveVersions(): List<LibraryDeclaration> {
        libraries.forEach { library ->
            if (library.versionRef != null && library.version == null) {
                library.version = versionRefs.find { version ->
                    version.name == library.versionRef
                }?.version ?: library.version
            }

            // todo: check if module containes : to be valid or not
            if (library.module != null && library.module.contains(":")) {
                val (group, name) = library.module.split(":")
                library.group = group
                library.name = name
            }

        }

        libraries.groupBy { it.group to it.name }.forEach { (groupNamePair, libraries) ->
            resolveLibraries(libraries)
        }

        return resolvedLibraries
    }

    private fun resolveLibraries(libraries: List<LibraryDeclaration>) {
        when (versionResolutionStrategy) {
            VersionResolutionStrategy.HIGHEST_VERSION -> resolveHighestVersion(libraries)
            VersionResolutionStrategy.LOWEST_VERSION -> resolveLowestVersion(libraries)
        }
    }

    private fun resolveHighestVersion(libraries: List<LibraryDeclaration>): List<LibraryDeclaration> {
        TODO()
    }

    private fun resolveLowestVersion(libraries: List<LibraryDeclaration>): List<LibraryDeclaration> {
        TODO()
    }
}