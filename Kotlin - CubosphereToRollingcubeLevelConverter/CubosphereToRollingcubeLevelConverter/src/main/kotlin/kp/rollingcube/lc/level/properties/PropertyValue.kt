package kp.rollingcube.lc.level.properties

import kotlin.math.absoluteValue

class PropertyValue private constructor(private var int: Int, private var float: Float, private var string: String)
{
    constructor(value: Int) : this(value, 0.0f, "")
    constructor(value: Float) : this(0, value, "")
    constructor(value: Boolean) : this(if(value) 1 else 0, 0.0f, "")
    constructor(value: String) : this(0, 0.0f, value)

    var intValue: Int get() = int; set(value) { int = value; float = 0.0f; string = ""; }
    var floatValue: Float get() = float; set(value) { int = 0; float = value; string = ""; }
    var booleanValue: Boolean get() = int != 0; set(value) { int = if(value) 1 else 0; float = 0.0f; string = ""; }
    var stringValue: String get() = string; set(value) { int = 0; float = 0.0f; string = value; }
    var enumOrdinalValue: Int get() = int.absoluteValue; set(value) { int = 0.coerceAtLeast(value); float = 0.0f; string = ""; }

    val copy: PropertyValue get() = PropertyValue(int, float, string)

    fun copyFrom(other: PropertyValue)
    {
        int = other.int
        float = other.float
        string = other.string
    }
    fun copyTo(other: PropertyValue) = other.copyFrom(this)

    companion object
    {
        val ZERO: PropertyValue get() = PropertyValue(0, 0.0f, "")

        fun Int.toPropertyValue() = PropertyValue(this)
        fun Float.toPropertyValue() = PropertyValue(this)
        fun Boolean.toPropertyValue() = PropertyValue(this)
        fun String?.toPropertyValue() = PropertyValue(this ?: "")

        fun String?.toIntPropertyValue() = if(this == null) 0.toPropertyValue() else toInt().toPropertyValue()
        fun String?.toFloatPropertyValue() = if(this == null) 0.0f.toPropertyValue() else toFloat().toPropertyValue()
        fun String?.toBooleanPropertyValue() = if(this == null) false.toPropertyValue() else toBoolean().toPropertyValue()
        fun String?.toStringPropertyValue() = toPropertyValue()
    }
}