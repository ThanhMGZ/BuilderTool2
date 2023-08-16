package vn.thanhmagics.buildertool2;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {

    private Player player;

    public PlayerData(Player player) {
        this.player = player;
        BuilderTool2.getInstance().getPlayerDataMap().put(player, this);
    }

    private Inventory inventory;

    private PC pc;

    private Undo undo;

    public Undo getUndo() {
        return undo;
    }

    public void setUndo(Undo undo) {
        this.undo = undo;
    }

    private Location pos1 = null, pos2 = null;

    public String str = null;

    public EditingWorld editingWorld = null;

    private boolean editMode = false;

    private boolean swem = false;

    public boolean isSwem() {
        return swem;
    }

    public PC getPc() {
        return pc;
    }

    public void setPc(PC pc) {
        this.pc = pc;
    }

    public void setSwem(boolean swem) {
        this.swem = swem;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public static Map<String,PC> pcMap = new HashMap<>();

    public static class Undo {
        private List<Block> old;
        private List<Block> nEw;

        private Structure structure;

        private Location center;
        public Undo(List<Block> old, List<Block> nEw,Structure structure,Location center) {
            this.old = old;
            this.nEw = nEw;
            this.structure = structure;
            this.center = center;
        }

        public Location getCenter() {
            return center;
        }

        public Structure getStructure() {
            return structure;
        }

        public void undo(World world) {
            for (Block block : nEw) {
                world.getBlockAt(block.getX(),block.getY(),block.getZ()).setType(Material.AIR);
            }
            for (Block block : old) {
                world.getBlockAt(block.getX(),block.getY(),block.getZ()).setType(block.getMaterial());
            }
        }

        public List<Block> getOld() {
            return old;
        }

        public void setOld(List<Block> old) {
            this.old = old;
        }

        public List<Block> getnEw() {
            return nEw;
        }

        public void setnEw(List<Block> nEw) {
            this.nEw = nEw;
        }
    }

    public static class PC {
        private Structure structure;
        private Location center;
        private List<Block> blocks;

        private String name;

        public PC(String name,Structure structure, Location center, List<Block> blocks) {
            this.structure = structure;
            this.center = center;
            this.blocks = blocks;
            this.name = name;
            pcMap.put(name,this);
        }

        public static void save(FileConfiguration config) {
            for (String id : pcMap.keySet()) {
                StringBuilder sb = new StringBuilder();
                PC pc = pcMap.get(id);
                String locStr = pc.center.getWorld().getName() + ";" + pc.center.getBlockX() + ";" + pc.center.getBlockY() + ";" + pc.center.getBlockZ();
                sb.append(pc.structure.getName()).append(",").append(locStr).append(",");
                for (Block block : pc.blocks) {
                    sb.append(block.getX()).append(";").append(block.getY()).append(";").append(block.getZ()).append(";").append(block.getMaterial().name()).append("#");
                }
                if (pc.blocks.size() > 0)
                    sb.deleteCharAt(sb.length() - 1);
                config.set("pc." + id, sb.toString());
            }
        }

        public static PC getPcByString(String s) {
            List<Block> blocks = new ArrayList<>();
            String s1 = s.split(",")[3];
            for (String k : s1.split("#")) {
                blocks.add(new Block(Integer.parseInt(k.split(";")[0]),Integer.parseInt(k.split(";")[1]),Integer.parseInt(k.split(";")[2]),k.split(";")[3]));
            }
            String s2 = s.split(",")[2];
            return new PC(s.split(",")[0],BuilderTool2.getInstance().getStructureMap().get(s.split(",")[1]),
                    new Location(Bukkit.getWorld(s2.split(";")[0]),Integer.parseInt(s2.split(";")[1]),Integer.parseInt(s2.split(";")[2]),Integer.parseInt(s2.split(";")[3])),
                    blocks);
        }

        public String getName() {
            return name;
        }

        public Structure getStructure() {
            return structure;
        }

        public Location getCenter() {
            return center;
        }

        public void setCenter(Location center) {
            this.center = center;
        }

        public List<Block> getBlocks() {
            return blocks;
        }

        public void setBlocks(List<Block> blocks) {
            this.blocks = blocks;
        }
    }

}
