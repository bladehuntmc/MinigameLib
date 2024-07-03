# MinigameLib

---

### Adding as a dependency

```kotlin
repositories {
    maven("https://mvn.bladehunt.net/releases")
}

dependencies {
    // kotlinx.coroutines is REQUIRED
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    implementation("net.bladehunt:minigame-lib:0.1.0-alpha.1")
}
```

---
MinigameLib is a library for making minigames in Minestom using a Kotlin DSL. Instances of minigames are run using
coroutines.

### Example

Visit [the example folder](/example) to view an example.

- Clone the repository: `git clone https://github.com/bladehuntmc/MinigameLib`
- Run the example in IntelliJ
- Join on two Minecraft instances
- Enter the command `/join` on both. (Note: The instance takes time to load, so if entered too quickly, the game will
  start before fully loaded)