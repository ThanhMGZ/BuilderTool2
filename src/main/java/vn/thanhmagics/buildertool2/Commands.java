package vn.thanhmagics.buildertool2;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Random;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender commandSender,  Command command,  String s,  String[] args) {
        Player player = (Player) commandSender;
        PlayerData playerData = BuilderTool2.getInstance().getPlayerDataMap().get(player);
        if (args.length == 0) {
            player.sendMessage("/bt cs [id] : copy structure");
            player.sendMessage("/bt ed [id] : edit structure");
            player.sendMessage("/bt dl [id] : delete structure");
            player.sendMessage("/bt sv [id] : save structure (trong edit world)");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("init")) {
                playerData.editingWorld.clearWorld();
                return true;
            } else if (args[0].equalsIgnoreCase("info")) {
                player.sendMessage(player.getWorld().getName());
                return true;
            } else if (args[0].equalsIgnoreCase("tp")) {
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                return true;
            } else if (args[0].equalsIgnoreCase("ss")) {
                if (playerData.editingWorld != null) {
                    playerData.setSwem(true);
                    playerData.setEditMode(true);
                    player.sendMessage("dap vao 2 block...");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("ud")) {
                if (playerData.getUndo() != null) {
                    playerData.getUndo().undo(player.getWorld());
                    player.sendMessage("undo success!");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("six")) {
                if (playerData.getUndo() != null) {
                    playerData.getUndo().undo(player.getWorld());
                    playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().add(1, 0, 0), player);
                }
            } else if (args[0].equalsIgnoreCase("siy")) {
                if (playerData.getUndo() != null) {
                    playerData.getUndo().undo(player.getWorld());
                    playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().add(0, 1, 0), player);
                }
            } else if (args[0].equalsIgnoreCase("siz")) {
                if (playerData.getUndo() != null) {
                    playerData.getUndo().undo(player.getWorld());
                    playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().add(0, 0, 1), player);
                }
            } else if (args[0].equalsIgnoreCase("sdx")) {
                if (playerData.getUndo() != null) {
                    playerData.getUndo().undo(player.getWorld());
                    playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().subtract(1, 0, 0), player);
                }
            } else if (args[0].equalsIgnoreCase("sdy")) {
                if (playerData.getUndo() != null) {
                    playerData.getUndo().undo(player.getWorld());
                    playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().subtract(0, 1, 0), player);
                }
            } else if (args[0].equalsIgnoreCase("sdz")) {
                if (playerData.getUndo() != null) {
                    playerData.getUndo().undo(player.getWorld());
                    playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().subtract(0, 0, 1), player);
                }
            } else {
                player.sendMessage("/bt cs [id] : copy structure");
                player.sendMessage("/bt ed [id] : edit structure");
                player.sendMessage("/bt dl [id] : delete structure");
                player.sendMessage("/bt sv [id] : save structure (trong edit world)");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("cs")) {
                if (!BuilderTool2.getInstance().getStructureMap().containsKey(args[1])) {
                    playerData.str = args[1];
                    playerData.setEditMode(true);
                    player.sendMessage("đập vào 2 block để tạo 2pos");
                } else {
                    player.sendMessage("structure da ton tai");
                }
            } else if (args[0].equalsIgnoreCase("ls")) {
                if (BuilderTool2.getInstance().getStructureMap().containsKey(args[1])) {
                    player.sendMessage("file da load truoc do");
                    return true;
                }
                File file = new File(BuilderTool2.getInstance().getDataFolder(),"structure/" + args[1] + ".yml");
                if (file.exists()) {
                    Structure.loadStructure(YamlConfiguration.loadConfiguration(file));
                    player.sendMessage("load thành công!");
                } else {
                    player.sendMessage("name ko ton tai");
                }
            } else if (args[0].equalsIgnoreCase("es")) {
                if (BuilderTool2.getInstance().getStructureMap().containsKey(args[1])) {
                    player.sendMessage("create world...");
                    new EditingWorld(BuilderTool2.getInstance().getStructureMap().get(args[1]),player).teleportToWorld();
                } else {
                    player.sendMessage("id ko ton tai");
                }
            } else if (args[0].equalsIgnoreCase("ps")) {
                if (BuilderTool2.getInstance().getStructureMap().containsKey(args[1])) {
                    player.sendMessage("starting...");
                    BuilderTool2.getInstance().getStructureMap().get(args[1]).paste(player.getLocation(),player);
                } else {
                    player.sendMessage("id ko ton tai");
                }
            } else if (args[0].equalsIgnoreCase("gt")) {
                World world = Bukkit.getWorld(args[1]);
                if (world != null) {
                    player.teleport(world.getSpawnLocation());
                } else {
                    player.sendMessage("world ko ton tai");
                }
            } else if (args[0].equalsIgnoreCase("iw")) {
                Bukkit.createWorld(new WorldCreator(args[1]));
                player.sendMessage("load success!");
            }            } else if (args[0].equalsIgnoreCase("six")) {
            if (playerData.getUndo() != null) {
                playerData.getUndo().undo(player.getWorld());
                playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().add(Integer.parseInt(args[1]), 0, 0), player);
            }
        } else if (args[0].equalsIgnoreCase("siy")) {
            if (playerData.getUndo() != null) {
                playerData.getUndo().undo(player.getWorld());
                playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().add(0, Integer.parseInt(args[1]), 0), player);
            }
        } else if (args[0].equalsIgnoreCase("siz")) {
            if (playerData.getUndo() != null) {
                playerData.getUndo().undo(player.getWorld());
                playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().add(0, 0, Integer.parseInt(args[1])), player);
            }
        } else if (args[0].equalsIgnoreCase("sdx")) {
            if (playerData.getUndo() != null) {
                playerData.getUndo().undo(player.getWorld());
                playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().subtract(Integer.parseInt(args[1]), 0, 0), player);
            }
        } else if (args[0].equalsIgnoreCase("sdy")) {
            if (playerData.getUndo() != null) {
                playerData.getUndo().undo(player.getWorld());
                playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().subtract(0, Integer.parseInt(args[1]), 0), player);
            }
        } else if (args[0].equalsIgnoreCase("sdz")) {
            if (playerData.getUndo() != null) {
                playerData.getUndo().undo(player.getWorld());
                playerData.getUndo().getStructure().paste(playerData.getUndo().getCenter().clone().subtract(0, 0, Integer.parseInt(args[1])), player);
            }
        } else if (args.length == 3) {

        } else if (args.length == 4) {

        }
        return true;
    }
}
