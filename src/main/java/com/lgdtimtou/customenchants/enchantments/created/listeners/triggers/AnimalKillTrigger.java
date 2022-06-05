package com.lgdtimtou.customenchants.enchantments.created.listeners.triggers;

import com.lgdtimtou.customenchants.enchantments.created.CustomEnchantBuilder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class AnimalKillTrigger extends Trigger{
    public AnimalKillTrigger(Enchantment enchantment, List<CustomEnchantBuilder.CustomEnchantLevelInfo> levels) {
        super(enchantment, levels);
    }

    @EventHandler
    public void onAnimalKill(EntityDeathEvent e){
        if (!(e.getEntity() instanceof Animals))
            return;
        if (!defaultChecks(e.getEntity().getKiller()))
            return;
        dispatchCommands();
    }
}
