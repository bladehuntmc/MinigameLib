package net.bladehunt.minigamelib.dsl

import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.element.GameElement
import net.bladehunt.minigamelib.instance.GameInstance

@DslMarker @Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE) annotation class DescriptorDsl

data class DescriptorBuilder<T : GameInstance<T>>(
    var name: String? = null,
    val elements: MutableList<GameElement<T>> = arrayListOf()
) {

    fun build(): GameDescriptor<T> = GameDescriptor(elements)
}

@DescriptorDsl
inline fun <T : GameInstance<T>> gameDescriptor(
    block: DescriptorBuilder<T>.() -> Unit
): GameDescriptor<T> = DescriptorBuilder<T>().apply(block).build()

@DescriptorDsl
inline fun <T : GameInstance<T>> DescriptorBuilder<T>.element(
    crossinline block:
        suspend context(T, GameElement<T>)
        () -> Unit
): GameElement<T> {
    val element =
        object : GameElement<T>() {
            context(T)
            override suspend fun run() {
                block(this@T, this)
            }
        }
    elements.add(element)
    return element
}
