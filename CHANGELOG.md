# 1.0.0

- Initial release

# 1.0.1

- Overhauled textures, courtesy of DurgerKing10!

# 1.0.2

- Updated to 1.16
- Made all instruments into one file (thanks LatvianModder!)
- Fixed thing where bottom section all sounded the same (thanks Lat again)

# 1.1.0

- Lost to time, apparently

# 1.2.0

- Playing the instrument now spawns a note particle, like Note Blocks do
- Punching a mob will spawn a cloud of note particles, and play a bunch of random notes
- Added three enchantments for instruments:
  - **Healing Beat** - Playing the instrument heals nearby non-monsters by a small amount
  - **BWAAAP** - Playing the instrument knocks back nearby mobs
  - **And His Music Was Electric** - While anywhere in your inventory, damage dealt will spread to nearby mobs with diminishing returns

# 1.3.0

- Updated to 1.19
- Instruments now have 100 durability, but this ONLY gets lowered when And His Music Was Electric is (successfully) triggered
- Custom sound for item breaking
- Fixed lightning waves sometimes overshooting
- Moved some configs from common to client
- Added a config so you can spawn the lightning wave all at once as a line instead
- BWAAAP is now stronger in the Y direction if sneaking
- Replaced several forEach(), should be more performant now
- Healing Beat only plays a particle over mobs that are actually healed
- Removed some redundant code

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


# 1.4.1

- Lot of refactors
- FIX AND HIS MUSIC WAS ELECTRIC

# 1.5.0

- Updated to 1.21
- And His Music Was Electric now works and looks nicer