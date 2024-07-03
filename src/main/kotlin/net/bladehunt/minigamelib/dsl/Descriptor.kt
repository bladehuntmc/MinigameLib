package net.bladehunt.minigamelib.dsl

import net.bladehunt.minigamelib.Game
import net.bladehunt.minigamelib.descriptor.GameDescriptor
import net.bladehunt.minigamelib.element.GameElement

@DslMarker
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
annotation class DescriptorDsl

data class DescriptorBuilder<S : Game.Scope>(
    var name: String? = null,
    val elements: MutableList<GameElement<S>> = arrayListOf()
) {
    operator fun GameElement<S>.unaryPlus() {
        elements.add(this)
    }

    fun build(): GameDescriptor<S> = GameDescriptor(requireNotNull(name), elements)
}

@DescriptorDsl
inline fun <S : Game.Scope> gameDescriptor(block: DescriptorBuilder<S>.() -> Unit): GameDescriptor<S> =
    DescriptorBuilder<S>().apply(block).build()

@DescriptorDsl
inline fun <S : Game.Scope> DescriptorBuilder<S>.element(crossinline block: suspend S.() -> Unit): GameElement<S> =
    GameElement { block() }