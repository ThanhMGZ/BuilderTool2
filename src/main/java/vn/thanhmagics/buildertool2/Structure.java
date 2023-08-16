package vn.thanhmagics.buildertool2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Structure {

    private List<Block> blocks = new ArrayList<>();

    private String name;

    public Structure(String name) {
        this.name = name;
        BuilderTool2.getInstance().getStructureMap().put(name, this);
    }

    public void paste(Location location, Player player) {;
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            List<Block> b = new ArrayList<>();
            List<Block> old = new ArrayList<>();
//            int i = 0;
            for (Block block : blocks) {
//                i++;
//                if (i % 100 == 0)
//                    player.sendMessage("paste progress:" + ChatColor.GOLD + " " + i + "/" + blocks.size());
                Location loc = new Location(location.getWorld(),
                        location.getBlockX() + block.getX(),location.getBlockY() + block.getY(),location.getBlockZ() + block.getZ());
                if (loc.getWorld() == null)
                    continue;
                Material material = loc.getWorld().getBlockAt(loc).getType();
                if (!material.equals(Material.AIR)) {
                    old.add(new Block(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ(), material.name()));
                }
                loc.getWorld().getBlockAt(loc)
                        .setType(block.getMaterial());
                b.add(new Block(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ(),block.getMaterial().name()));
            }
            PlayerData playerData = BuilderTool2.getInstance().getPlayerDataMap().get(player);
//        playerData.setPc(new PlayerData.PC(id,this,location,b));
            playerData.setUndo(new PlayerData.Undo(old,b,this,location));
//        },executorService);
//        future.thenAccept(unused -> player.sendMessage("Paste Success!"));
//        executorService.shutdown();
        player.sendMessage("Paste Success!");
    }

    public static List<Block> getBlocksInBetween2P(Location loc1, Location loc2) {
        int lowX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int lowY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int lowZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        List<Block> locs = new ArrayList<>();

        for (int x = 0; x < Math.abs(loc1.getBlockX() - loc2.getBlockX()); x++) {
            for (int y = 0; y < Math.abs(loc1.getBlockY() - loc2.getBlockY()); y++) {
                for (int z = 0; z < Math.abs(loc1.getBlockZ() - loc2.getBlockZ()); z++) {
                    // locs.add(new Location(loc1.getWorld(),lowX+x, lowY+y, lowZ+z));
                    org.bukkit.block.Block block = (loc1.getWorld().getBlockAt(lowX + x, lowY + y, lowZ + z));
                    locs.add(new Block(block.getX(), block.getY(), block.getZ(), block.getType().name()));
                }
            }
        }

        return transform(locs);
    }


    public static List<Block> transform(List<Block> blocks) {
        List<Block> rs = new ArrayList<>();
        int minX = Integer.MAX_VALUE, maxX = 0,
                minY = Integer.MAX_VALUE, maxY = 0,
                minZ = Integer.MAX_VALUE, maxZ = 0;
        for (Block block : blocks) {
            minX = Math.min(minX, block.getX());
          //  maxX = Math.max(maxX, block.getX());
            minY = Math.min(minY, block.getY());
          //  maxY = Math.max(maxY, block.getY());
            minZ = Math.min(minZ, block.getZ());
           // maxZ = Math.max(maxZ, block.getZ());
        }
//        int cx = ((maxX - minX) / 2) + minX;
//        int cy = ((maxY - minY) / 2) + minY;
//        int cz = ((maxZ - minZ) / 2) + minZ;
        for (Block block : blocks) {
            int dx = (int) block.getX() - minX;
            int dy = (int) block.getY() - minY;
            int dz = (int) block.getZ() - minZ;
            if (block.getMaterial().equals(Material.AIR))
                continue;
            rs.add(new Block(dx, dy, dz, block.getMaterial().name()));
        }
        Bukkit.getPlayer("thanhmagics").sendMessage(String.valueOf(rs.size()));
        return rs;
    }

    public static Structure loadStructure(FileConfiguration config) {
        List<String> data = config.getStringList("data");
        List<Block> blocks = new ArrayList<>();
        for (String dt : data) {
            blocks.add(new Block(Integer.parseInt(dt.split(" ")[0]),
                    Integer.parseInt(dt.split(" ")[1]),
                    Integer.parseInt(dt.split(" ")[2]),
                    (dt.split(" ")[3])));
        }
        Structure structure = new Structure(config.getString("sn"));
        structure.setBlocks(blocks);
        return structure;
    }

    public String getName() {
        return name;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
