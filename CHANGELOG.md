# 1.4.0

- Now required Kotlin for Forge (and, obviously, the mod has been ported to Kotlin)
- Various code improvements, should perform better now (though it didn't really perform poorly before)
  - For example, previously some client configs were being checked on the server. This is fixed.
- And His Music Was Electric stops spreading when it would deal <0.5 damage.
- Monsters with an empty main hand have a 0.5% (configurable) chance of spawning with a random instrument in their hand
  - Shows off how it does a bunch of sounds at once when attacking
  - I plan to eventually make this its own entity type, a la Instrumental Mobs
    - I also plan on making them play series of notes rather than just one random one, but that requires figuring out how to either use midi files or making my own shittier version 
- Configs look nicer
- All configs now use doubles instead of floats
