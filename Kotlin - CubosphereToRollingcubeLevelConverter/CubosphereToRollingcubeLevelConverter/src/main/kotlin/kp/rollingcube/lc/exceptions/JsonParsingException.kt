package kp.rollingcube.lc.exceptions

class JsonParsingException : ConverterException
{
    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable?) : super(message, cause)
}