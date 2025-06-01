package be.timonc.customenchantments.enchantments.created.fields.instructions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.regex.Pattern;

public class InstructionCleanupListeners implements Listener {

    private static final Pattern pattern = Pattern.compile("^/?(stop|restart|reload)\\b.*", Pattern.CASE_INSENSITIVE);

    @EventHandler
    public void onServerStop(ServerCommandEvent event) {
        checkServerShutdownCommand(event.getCommand());
    }

    @EventHandler
    public void onPlayerServerStop(PlayerCommandPreprocessEvent event) {
        checkServerShutdownCommand(event.getMessage());
    }

    @EventHandler
    public void onRemoteServerStop(RemoteServerCommandEvent event) {
        checkServerShutdownCommand(event.getCommand());
    }

    private void checkServerShutdownCommand(String command) {
        if (pattern.matcher(command).matches())
            InstructionCall.callCleanupCommands();
    }
}
