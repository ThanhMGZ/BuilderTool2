package vn.thanhmagics.buildertool2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    @EventHandler
    public void oj(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!BuilderTool2.getInstance().getPlayerDataMap().containsKey(player)) {
            new PlayerData(player);
        }
    }

    @EventHandler
    public void oq(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BuilderTool2.getInstance().getPlayerDataMap().remove(player);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    @EventHandler
    public void bb(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = BuilderTool2.getInstance().getPlayerDataMap().get(player);
        if (playerData.isEditMode()) {
            if (!event.getBlock().getType().equals(Material.AIR))
                event.setCancelled(true);
            if (playerData.getPos1() == null) {
                playerData.setPos1(event.getBlock().getLocation());
                player.sendMessage("set pos 1 thanh cong");
                return;
            }
            if (playerData.getPos2() == null) {
                playerData.setPos2(event.getBlock().getLocation());
                player.sendMessage("set pos 2 thanh cong");
            }
            if (!playerData.isSwem()) {
                playerData.setEditMode(false);
                Structure structure = new Structure(playerData.str);
                structure.setBlocks(Structure.getBlocksInBetween2P(playerData.getPos1(), playerData.getPos2()));
               // player.sendMessage("tạo world....");
                new EditingWorld(structure, player).saveWorld(playerData.getPos1(),playerData.getPos2(),false);
                playerData.setPos1(null);
                playerData.setPos2(null);
                player.sendMessage("save structure thành công với id là " + ChatColor.GOLD + structure.getName());
            } else {
                player.sendMessage("starting...");
                playerData.editingWorld.saveWorld(playerData.getPos1(),playerData.getPos2(),true);
                playerData.setSwem(false);
                playerData.setEditMode(false);
            }
        }
    }

}
