package com.bgsoftware.superiorskyblock.commands.command;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPermission;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.wrappers.SSuperiorPlayer;
import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.commands.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CmdName implements ICommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("name", "setname", "rename");
    }

    @Override
    public String getPermission() {
        return "superior.island.name";
    }

    @Override
    public String getUsage() {
        return "island name <island-name>";
    }

    @Override
    public String getDescription() {
        return Locale.COMMAND_DESCRIPTION_NAME.getMessage();
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(sender);
        Island island = superiorPlayer.getIsland();

        if(island == null){
            Locale.INVALID_ISLAND.send(superiorPlayer);
            return;
        }

        if(!superiorPlayer.hasPermission(IslandPermission.CHANGE_NAME)){
            Locale.NO_NAME_PERMISSION.send(superiorPlayer, island.getRequiredRole(IslandPermission.CHANGE_NAME));
            return;
        }

        String islandName = args[1];

        if(islandName.length() > plugin.getSettings().islandNamesMaxLength){
            Locale.NAME_TOO_LONG.send(superiorPlayer);
            return;
        }

        if(islandName.length() < plugin.getSettings().islandNamesMinLength){
            Locale.NAME_TOO_SHORT.send(superiorPlayer);
            return;
        }

        if(plugin.getSettings().filteredIslandNames.stream().anyMatch(name -> name.equalsIgnoreCase(islandName))){
            Locale.NAME_BLACKLISTED.send(superiorPlayer);
            return;
        }

        if(island.getName().equals(islandName)){
            Locale.SAME_NAME_CHANGE.send(superiorPlayer);
            return;
        }

        if(!island.getName().equalsIgnoreCase(islandName) && plugin.getGrid().getIsland(islandName) != null){
            Locale.ISLAND_ALREADY_EXIST.send(superiorPlayer);
            return;
        }

        island.setName(islandName);

        for(Player player : Bukkit.getOnlinePlayers())
            Locale.NAME_ANNOUNCEMENT.send(player, superiorPlayer.getName(), islandName);

        Locale.CHANGED_NAME.send(superiorPlayer, islandName);
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}