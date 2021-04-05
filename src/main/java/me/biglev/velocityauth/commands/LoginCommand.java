package me.biglev.velocityauth.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.biglev.velocityauth.utils.ComponentFormat;
import me.biglev.velocityauth.utils.Core;
import me.biglev.velocityauth.utils.settings.Manager;

public class LoginCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        Player player = (Player) source;

        if (!Manager.getSettings().getAuthServers().contains(player.getCurrentServer().get().getServerInfo().getName())) {
            player.sendMessage(ComponentFormat.format(Manager.getMessage().getError_messages().getLogin_required()));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ComponentFormat.format(Manager.getMessage().getLogin().getCommand_usage()));
            return;
        }

        String password = args[0];
        Core.loginCommand(player, password);
    }
}
