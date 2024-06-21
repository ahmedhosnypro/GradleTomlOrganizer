package resolver

import dependency.BundleDeclaration
import dependency.DependencyNode
import dependency.LibraryDeclaration
import dependency.PluginDeclaration
import dependency.VersionDeclaration


class ConflictResolver(
    private val dependencyNodes: List<DependencyNode>,
    val versionResolutionStrategy: VersionResolutionStrategy = VersionResolutionStrategy.HIGHEST_VERSION,
    // todo: implement version resolution channel
    private val versionResolutionChannel: VersionResolutionChannel = VersionResolutionChannel.RELEASE
) {

    val versionRefs: List<VersionDeclaration>
        get() = dependencyNodes.filterIsInstance<VersionDeclaration>()

    val libraries: List<LibraryDeclaration>
        get() = dependencyNodes.filterIsInstance<LibraryDeclaration>()

    val bundles: List<BundleDeclaration>
        get() = dependencyNodes.filterIsInstance<BundleDeclaration>()

    val plugins: List<PluginDeclaration>
        get() = dependencyNodes.filterIsInstance<PluginDeclaration>()

    val resolvedVersions = mutableListOf<VersionDeclaration>()
    val resolvedLibraries = mutableListOf<LibraryDeclaration>()
    val resolvedBundles = mutableListOf<BundleDeclaration>()
    val resolvedPlugins = mutableListOf<PluginDeclaration>()

    init {
        resolveVersionRefVersions()
        resolveLibraryVersions()
        resolveConflictBundles()
        resolvePluginVersions()
    }
}