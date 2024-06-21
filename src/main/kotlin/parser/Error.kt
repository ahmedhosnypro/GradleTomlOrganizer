package parser

class InvalidPluginDeclarationError(message: String) : Exception(message)
class InvalidLibraryDeclarationError(message: String) : Exception(message)
class InvalidVersionDeclarationError(message: String) : Exception(message)
class InvalidBundleDeclarationError(message: String) : Exception(message)