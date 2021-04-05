package me.biglev.velocityauth.listeners;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import me.biglev.velocityauth.Main;
import me.biglev.velocityauth.utils.ComponentFormat;
import me.biglev.velocityauth.utils.api.PlayerAPI;
import me.biglev.velocityauth.utils.settings.Manager;

public class PlayerCommandHandler {

    @Subscribe(order = PostOrder.NORMAL)
    public void onCommand(CommandExecuteEvent e) {
        Player player = (Player) e.getCommandSource();

        PlayerAPI playerAPI = Main.getPlayerAPIList().searchPlayer(player.getUsername());
        if (!playerAPI.isLogged()) {
            if (!Manager.getSettings().getCommands().contains(e.getCommand().split(" ")[0])) {
                e.setResult(CommandExecuteEvent.CommandResult.denied());
                player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogin_required()));
                return;
            }
        }

    }
}
