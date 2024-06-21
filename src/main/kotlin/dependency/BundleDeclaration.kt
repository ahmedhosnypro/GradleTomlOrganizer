package dependency

data class BundleDeclaration(val name: String, val libraries: List<String>) : DependencyNode() {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(name).append(" = [")
        var lineLength = name.length + 3
        libraries.forEach {
            if (lineLength + it.length + 2 > 80) {
                sb.append("\n    ")
                lineLength = 4
            }
            sb.append("\"$it\"").append(", ")
            lineLength += it.length + 2
        }
        sb.delete(sb.length - 2, sb.length)
        sb.append("]")
        return sb.toString()
    }
}
