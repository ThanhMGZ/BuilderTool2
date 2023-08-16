package vn.thanhmagics.buildertool2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BuilderTool2 extends JavaPlugin {

    private static BuilderTool2 instance;

    private Map<String, Structure> structureMap = new HashMap<>();

    private Map<Player, PlayerData> playerDataMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("buildertool").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        saveDefaultConfig();
        saveResource("structure/tesst.yml",true);
        instance = this;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!BuilderTool2.getInstance().getPlayerDataMap().containsKey(player)) {
                new PlayerData(player);
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (EditingWorld.wns.contains(player.getWorld().getName())) {
                Bukkit.dispatchCommand(player,"bt tp");
            }
        }
        for (String wn : EditingWorld.wns) {
            File wp = Objects.requireNonNull(Bukkit.getWorld(wn)).getWorldFolder();
            Bukkit.unloadWorld(wn,false);
            try {
                Files.deleteIfExists(wp.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Map<Player, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public Map<String, Structure> getStructureMap() {
        return structureMap;
    }

    public static BuilderTool2 getInstance() {
        return instance;
    }
}
