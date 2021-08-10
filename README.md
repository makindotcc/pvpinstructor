### Build plugin jar

```./gradlew jar```

### Running

To run this plugin you must include Kotlin 1.4.32+ to the server classpath somehow. \
Example two ways of doing this: \
1. Put [this plugin](https://www.spigotmc.org/resources/kotlin.80808/) into plugins directory.
2. (More advanced way, probably not supported on most server hostings)
   Use "-cp" jvm argument to include custom jars to the classpath. \
   Example: ``java -cp "spigot-server.jar:libs/kotlin-stdlib.jar" org.bukkit.craftbukkit.Main``
