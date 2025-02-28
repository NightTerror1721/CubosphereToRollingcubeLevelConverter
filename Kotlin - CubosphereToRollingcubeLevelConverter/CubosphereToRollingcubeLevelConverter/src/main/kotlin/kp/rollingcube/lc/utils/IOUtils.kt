package kp.rollingcube.lc.utils

import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths



object IOUtils
{
    const val CUBOSPHERE_LEVEL_FORMAT = "ldef"
    const val ROLLINGCUBE_LEVEL_FORMAT = "json"

    const val CUBOSPHERE_LEVEL_DOT_FORMAT = ".$CUBOSPHERE_LEVEL_FORMAT"
    const val ROLLINGCUBE_LEVEL_DOT_FORMAT = ".$ROLLINGCUBE_LEVEL_FORMAT"


    fun readAll(reader: Reader): String
    {
        val sb = StringBuilder()
        val buffer = CharArray(8192)

        var len = reader.read(buffer, 0, buffer.size)
        while(len > 0) {
            sb.append(buffer)
            len = reader.read(buffer, 0, buffer.size)
        }

        return sb.toString()
    }
    fun readAll(input: InputStream) = readAll(InputStreamReader(input))

    fun readAll(readAllFromFile: Path) = Files.newBufferedReader(readAllFromFile).use { readAll(it) }

    val userDirectory: Path get() = Paths.get(System.getProperty("user.dir")).toAbsolutePath()

    fun getFileName(file: Path): String {
        val name = file.fileName.toString()
        val index = name.lastIndexOf('.')
        return if (index < 0) name else name.substring(0, index)
    }

    fun getFileExtension(file: Path): String {
        val name = file.fileName.toString()
        val index = name.lastIndexOf('.')
        return if (index < 0) "" else name.substring(index + 1)
    }

    fun concatElementAtPathEnd(path: Path, element: String): Path {
        if (path.parent == null)
            return Paths.get(path.fileName.toString() + element)
        return path.parent.resolve(path.fileName.toString() + element)
    }

    fun getClasspathResourceUrl(path: String): URL? {
        var vpath = path
        if (!vpath.startsWith("/"))
            vpath = "/$vpath"
        return IOUtils::class.java.getResource(vpath)
    }

    fun getClasspathResourceAsStream(path: String): InputStream? {
        var vpath = path
        if (!path.startsWith("/")) vpath = "/$vpath"
        return IOUtils::class.java.getResourceAsStream(vpath)
    }
}