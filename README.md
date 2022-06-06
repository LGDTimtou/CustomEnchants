# CustomEnchants (WIP)
A MineCraft plugin that allows you to create and use custom enchants on your server!

# Features
- Default Custom Enchantments (Replenish, Telekenesis, ...)
- Command to add/remove custom enchantments from items (enchanted books coming soon)
- Ability to create fully custom enchantments via a yaml file (GUI coming soon)
  - Customizable:
    - Name
    - Max level
    - Events that triggers the enchantment (breaking a block, killing a mob, etc...)
    - Commands that are executed by the console when the enchantment triggers (Customizable for each level)
      - These commands can contain parameters (%x%, %y%, %z%, %player%, %killer%, etc...)
      - These commands can contain built in functions, Syntax: $[function(v1, v2, ...)] (add, subtract, multiply, ...)
    - The chance of these commands executing
    - Whether the event that triggers the enchantment should be cancelled
   
