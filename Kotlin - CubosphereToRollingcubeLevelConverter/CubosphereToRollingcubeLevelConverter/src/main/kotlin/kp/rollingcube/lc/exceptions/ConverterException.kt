package kp.rollingcube.lc.exceptions

open class ConverterException : Exception
{
    constructor(message: String, cause: Throwable?) : super(message, null)
    constructor(message: String) : super(message, null)
}