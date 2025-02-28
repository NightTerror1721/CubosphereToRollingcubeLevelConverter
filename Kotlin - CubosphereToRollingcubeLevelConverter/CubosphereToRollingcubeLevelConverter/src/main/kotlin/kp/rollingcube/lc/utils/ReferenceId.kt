package kp.rollingcube.lc.utils

abstract class ReferenceId protected constructor(private val code: Int)
{
    val isValid: Boolean get() = code > 0
    val isInvalid: Boolean get() = code <= 0

    fun toInt() = code
    override fun toString() = code.toString()

    abstract class BaseGenerator<T : ReferenceId> : Iterator<T>
    {
        private var id = 1

        override fun hasNext() = true
        override fun next() = generate()

        fun generate() = createNew(id++)

        protected abstract fun createNew(code: Int): T
    }
}