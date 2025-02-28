package kp.rollingcube.lc.utils

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kp.rollingcube.lc.exceptions.JsonParsingException
import java.io.IOException


class Version(
    val major: Int = 0,
    val minor: Int = 0,
    val patch: Int = 0,
    val build: Int = 0
) : Comparable<Version?>
{
    val isZero: Boolean get() = major == 0 && minor == 0 && patch == 0 && build == 0

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(major).append('.').append(minor)
        if (patch > 0) {
            sb.append('.').append(patch)
            if (build > 0) sb.append('.').append(build)
        } else if (build > 0) sb.append(".0.").append(build)
        return sb.toString()
    }

    override operator fun compareTo(other: Version?): Int
    {
        if (other == null)
            return 1

        var cmp = major.compareTo(other.major)
        if (cmp != 0) return cmp

        cmp = minor.compareTo(other.minor)
        if (cmp != 0) return cmp

        cmp = patch.compareTo(other.patch)
        if (cmp != 0) return cmp

        return build.compareTo(other.build)
    }

    companion object
    {
        private val PATTERN = Regex("^[0-9]+(.[0-9]+){0,3}\$")

        val APP_VERSION = Version(0, 3)
        val CURRENT_LEVEL_VERSION = Version(1)
        val ZERO = Version()

        @Throws(JsonParsingException::class)
        fun parse(text: String): Version
        {
            val trimText = text.trim { it <= ' ' }
            if (trimText.isBlank())
                throw JsonParsingException("Invalid Version format")

            if (!PATTERN.matches(trimText))
                throw JsonParsingException("Invalid Version format")

            try {
                val parts = trimText.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                if (parts.isEmpty())
                    throw JsonParsingException("Invalid Version format")

                return Version(
                    major = parts[0].toInt(),
                    minor = if(parts.size > 1) parts[1].toInt() else 0,
                    patch = if(parts.size > 2) parts[2].toInt() else 0,
                    build = if(parts.size > 3) parts[3].toInt() else 0
                )
            } catch (ex: NumberFormatException) {
                throw JsonParsingException("Invalid Version format", ex)
            }
        }
    }


    class Serializer : StdSerializer<Version> {
        constructor() : super(Version::class.java)
        constructor(cls: Class<Version>?) : super(cls)

        @Throws(IOException::class)
        override fun serialize(value: Version, gen: JsonGenerator, provider: SerializerProvider) {
            gen.writeString(value.toString())
        }
    }

    class Deserializer : StdDeserializer<Version?> {
        constructor() : super(Version::class.java)
        constructor(cls: Class<Version>?) : super(cls)

        @Throws(IOException::class, JacksonException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Version {
            val node = p.codec.readTree<JsonNode>(p)
            if (!node.isTextual)
                return ZERO

            return try {
                parse(node.asText())
            } catch (ex: JsonParsingException) {
                ZERO
            }
        }
    }
}