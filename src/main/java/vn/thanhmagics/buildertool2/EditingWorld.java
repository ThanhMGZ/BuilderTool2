package vn.thanhmagics.buildertool2;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.world.level.GameRules;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditingWorld {

    private Structure structure;

    private Player player;

    private World world;

    private String wn = "editing_" + new Random().nextInt(1000000);

    public static List<String> wns = new ArrayList<>();

    public EditingWorld(Structure structure, Player player) {
        this.structure = structure;
        this.player = player;
      //  world = Bukkit.createWorld(new WorldCreator(wn).type(WorldType.FLAT));
        //teleportToWorld();
    }

    public void teleportToWorld() {
        world = Bukkit.createWorld(new WorldCreator(wn).type(WorldType.FLAT));
        world.setGameRuleValue("randomTickSpeed", "0");
        player.teleport(new Location(world, 0, 5, 0));
        wns.add(wn);
        BuilderTool2.getInstance().getPlayerDataMap().get(player).editingWorld = this;
    }

    int minX = Integer.MAX_VALUE, maxX = 0, minZ = Integer.MAX_VALUE, maxZ = 0,minY = Integer.MAX_VALUE,maxY = 0;

    public void clearWorld() {
        player.sendMessage("starting....");
        for (Block block : structure.getBlocks()) {
            maxX = Math.max(maxX, block.getX());
            minX = Math.min(minX, block.getX());
            maxZ = Math.max(maxZ, block.getZ());
            minZ = Math.min(minZ, block.getZ());
            maxY = Math.max(maxY, block.getY());
            minY = Math.min(minY, block.getY());
        }
//        long t = System.currentTimeMillis();
//        for (int x = -(maxX+50); x < maxX+50; x++) {
//            for (int z = -(maxZ+50); z < maxZ+50; z++) {
//                for (int y = 1; y <= 13; y++) {
//                    if (!world.getBlockAt(x,y,z).getType().equals(Material.AIR)) {
//                        world.getBlockAt(x,y,z).setType(Material.AIR);
//                    }
//                }
//            }
//        }
//        player.sendMessage("clear world in: " + (System.currentTimeMillis() - t));
        initWorld();
    }

    private void initWorld() {
        long t1 = System.currentTimeMillis();
        for (int x = -(maxX+50); x < maxX+50; x++) {
            for (int z = -(maxZ+50); z < maxZ+50; z++) {
                world.getBlockAt(x,0,z).setType(Material.WHITE_WOOL);
            }
        }
        int yn;
        if (maxY + 20 < 256) {
            yn = (maxY - minY / 2) + 20;
        } else {
            yn = (maxY - minY / 2);
        }
        player.sendMessage("xây nền trong: " + (System.currentTimeMillis() - t1));
        for (Block block : structure.getBlocks()) {
            world.getBlockAt(block.getX(), yn + block.getY(), block.getZ()).setType(block.getMaterial());
        }
        player.sendMessage("built in: " + (System.currentTimeMillis() - t1));
    }

    public void saveWorld(Location p1,Location p2,boolean tp) {
        List<Block> blocks = Structure.getBlocksInBetween2P(p1,p2);
        final List<String>[] data = new List[]{new ArrayList<>()};
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            long t1 = System.currentTimeMillis();
            player.sendMessage("starting...");
//            int ly = Integer.MAX_VALUE;
//            for (int x = minX - 100; x < maxX + 100; x++) {
//                for (int z = minZ - 100; z < maxZ + 100; z++) {
//                    for (int y = 0; y < 256; y++) {
//                        if (!world.getBlockAt(x, y, z).getType().equals(Material.AIR)) {
//                            ly = Math.min(ly, y);
//                            data[0].add(x + " " + y + " " + z + " " + world.getBlockAt(x, y, z).getType().name());
//                        }
//                    }
//                }
//            }
            for (Block block : blocks) {
                int x = block.getX(),y = block.getY(),z = block.getZ();
                data[0].add(x + " " + y + " " + z + " " + block.getMaterial().name());
            }
//            List<String> ndt = new ArrayList<>();
//            for (String dt : data[0]) {
//                ndt.add(dt.split(" ")[0] + " " + (Integer.valueOf(dt.split(" ")[1])) + " " + dt.split(" ")[2] + " " + dt.split(" ")[3]);
//            }
//            data[0] = ndt;
            player.sendMessage("done in: " + (System.currentTimeMillis() - t1));
        }, executorService);
        future.thenAccept(unused -> {
            player.sendMessage("saving to FileConfig...");
            File file = new File(BuilderTool2.getInstance().getDataFolder(), "structure/" + structure.getName() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("sn",structure.getName());
            config.set("data",data[0]);
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            player.sendMessage("success!");
        });
        executorService.shutdown();
        if (tp) {
            Bukkit.dispatchCommand(player,"bt tp");
        }
        BuilderTool2.getInstance().getPlayerDataMap().get(player).editingWorld = null;
        //BuilderTool2.getInstance().getStructureMap().remove(structure.getName(),structure);
        Structure.loadStructure(YamlConfiguration.loadConfiguration(new File(BuilderTool2.getInstance().getDataFolder(),"structure/" + structure.getName() + ".yml")));
    }


    public Player getPlayer() {
        return player;
    }

    public Structure getStructure() {
        return structure;
    }
}
