package net.bladehunt.minigamelib.dsl

import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.element.GameElement

@DslMarker @Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE) annotation class DescriptorDsl

@JvmInline
value class DescriptorBuilder(private val elements: MutableList<GameElement>) {
    operator fun GameElement.unaryPlus() {
        elements.add(this)
    }

    fun build(): GameDescriptor = GameDescriptor(elements.toList())
}

@DescriptorDsl
inline fun gameDescriptor(block: DescriptorBuilder.() -> Unit): GameDescriptor =
    DescriptorBuilder(arrayListOf()).apply(block).build()

@DescriptorDsl
inline fun element(crossinline block: suspend GameElement.() -> Unit): GameElement =
    object : GameElement() {
        override suspend fun run() {
            block(this)
        }
    }
