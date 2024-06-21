package resolver

enum class VersionResolutionStrategy {
    HIGHEST_VERSION,
    LOWEST_VERSION,
}

enum class VersionResolutionChannel {
    RELEASE,
    SNAPSHOT,
    BETA,
    ALPHA,
    RC,
    MILESTONE,
}