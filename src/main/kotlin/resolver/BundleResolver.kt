package resolver

import BundleDeclaration

fun ConflictResolver.resolveConflictBundles() {
    bundles.groupBy { it.name }.forEach { (name, bundles) ->
        val allLibs = bundles.flatMap { it.libraries }
        resolvedBundles.add(
            BundleDeclaration(
                name = name,
                libraries = allLibs
            )
        )
    }
}