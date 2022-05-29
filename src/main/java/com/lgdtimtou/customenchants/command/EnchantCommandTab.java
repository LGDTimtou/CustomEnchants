package com.lgdtimtou.customenchants.command;

import com.lgdtimtou.customenchants.CustomEnchant;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EnchantCommandTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return null;
        return switch(args.length){
            //Returns the names of all the custom enchants
            case 1 -> {
                Stream<CustomEnchant> filtered = Arrays.stream(CustomEnchant.values())
                        .filter(ce -> ce.getName().toLowerCase().contains(args[0].toLowerCase()));
                yield filtered.map(ce -> ce.getName().toLowerCase()).collect(Collectors.toList());
            }
            //Checks if the given enchant at place 0 exists if so
            // it'll return a list of all possible levels for that enchant else -1
            case 2 -> {
                String enchant = args[0].toUpperCase();
                if (Arrays.stream(CustomEnchant.values()).anyMatch(v -> v.name().equals(enchant))){
                    int maxLvl = CustomEnchant.valueOf(enchant).getEnchantment().getMaxLevel();
                    yield IntStream.range(1, maxLvl + 1).mapToObj(Integer::toString).collect(Collectors.toList());
                } else {
                    yield List.of("-1");
                }
            }
            default -> null;
        };
    }
}
