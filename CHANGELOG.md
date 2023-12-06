# 1.3.2

- Now required Kotlin for Forge (and, obviously, the mod has been ported to Kotlin)
- Various code improvements, should perform better now (though it didn't really perform poorly before)
  - For example, previously some client configs were being checked on the server. This is fixed.
- And His Music Was Electric stops spreading when it would deal <0.5 damage.
- Monsters with an empty main hand have a 0.5% (configurable) chance of spawning with a random instrument in their hand
- Configs look nicer