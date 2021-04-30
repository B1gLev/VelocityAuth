package me.biglev.velocityauth.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.ComponentFormat;
import me.biglev.velocityauth.utils.api.PlayerProfile;
import me.biglev.velocityauth.utils.settings.Manager;

public class PlayerCommandHandler {

    @Subscribe(order = PostOrder.NORMAL)
    public void onCommand(CommandExecuteEvent e) {

        if (!(e.getCommandSource() instanceof ConsoleCommandSource)) {
            Player player = (Player) e.getCommandSource();
            PlayerProfile playerProfile = Main.getPlayerAPIList().searchPlayer(player.getUsername());

            if (!playerProfile.isLogged()) {
                if (!Manager.getSettings().getCommands().contains(e.getCommand().split(" ")[0])) {
                    e.setResult(CommandExecuteEvent.CommandResult.denied());
                    player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogin_required()));
                }
            }
        }

    }
}
