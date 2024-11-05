# CustomEnchants (WIP)
A MineCraft plugin that allows you to create and use custom enchants on your server!

# Features
- Built-in Custom Enchantments
- Command to add/remove custom enchantments from items
- Ability to create fully custom enchantments via a yaml file (GUI coming soon)
  - Customizable:
    - Name
    - Max level
    - Events that trigger the enchantment
    - Target items that can have the enchantment
    - Cooldowns specific for each level
    - Commands that are executed by the console when the enchantment triggers (Customizable for each level)
      - These commands can contain parameters 
      - These commands can contain built-in functions
    - The chance of these commands executing
    - Whether the event that triggers the enchantment should be cancelled

# Built-in Custom Enchants
- **Replenish**
  - Will replenish plants that have been harvested if the corresponding seed is in the player's inventory
  - Max level: 1

- **Telekinesis**
  - Adds all dropped items to the player's inventory, will be dropped if full
  - Max level: 1

   
# Global Command Parameters
- **%player%**: The player that triggered the enchantment
- **%x%**: The x coordinate of the player
- **%y%**: The y coordinate of the player
- **%z%**: The z coordinate of the player


# Triggers
- **break_block**
  - Triggers when a block is broken by the player
  - Parameters
    - %block_x% The x coordinate of the block
    - %block_y% The y coordinate of the block
    - %block_z% The z coordinate of the block
      <br></br>
- **damage_entity**
  - Triggers when an entity is damaged by the player
  - Overrides: damage_player, damage_animal, damage_mob
<br></br>
- **damage_player**
  - Triggers when a player is damaged by the player
  - Parameters
    - %damaged%: The person who got damaged
<br></br>
- **damage_mob**
  - Triggers when a mob is damaged by the player
<br></br>
- **damage_animal**
  - Triggers when an animal is damager by the player
<br></br>
- **kill_entity**
  - Triggers when an entity is killed by the player
  - Overrides: kill_player, kill_mob, kill_animal
<br></br>
- **kill_player**
  - Triggers when a player is killed by the player
  - Parameters
    - %killed%: The player who got killed
<br></br>
- **kill_mob**
  - Triggers when a mob is killed by the player
<br></br>
- **kill_animal**
  - Triggers when an animal is killed by the player
<br></br>
- **take_damage_from_nonentity**
  - Triggers when the player takes damage not from an entity (fall damage for example)
<br></br>
- **take_damage_from_entity**
  - Triggers when the player takes damage from an entity
  - Overrides: take_damage_from_player, take_damage_from_mob
<br></br>
- **take_damage_from_player**
  - Triggers when the player takes damage from another player
  - Parameters
    - %damager%: The person that damaged the player
<br></br>
- **take_damage_from_mob**
  - Triggers when the player takes damage from a mob
<br></br>
- **fishing_rod_caught**
  - Triggers when the player catches something with a fishing rod
<br></br>
- **fishing_rod_hit_player**
  - Triggers when the fishing rod bobber hits a player
  - Parameters:
    - %damaged%: The player that got hit by the bobber


# Built-in functions

**Syntax:** **$[function(v1, v2, ...)]**

- **add**
  - Adds numbers together
- **sub**
  - Subtracts numbers from each other
- **mul**
  - Multiplies numbers with each other
