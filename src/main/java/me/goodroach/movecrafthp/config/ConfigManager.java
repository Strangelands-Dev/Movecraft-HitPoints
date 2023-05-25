package me.goodroach.movecrafthp.config;

import me.goodroach.movecrafthp.MovecraftHitPoints;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {
    private File configFile;
    private FileConfiguration config;

    public ConfigManager() {
        configFile = new File(MovecraftHitPoints.getInstance().getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void listReader(String configPath, Map<Material, Double> map) {
        ConfigurationSection modifierSection = config.getConfigurationSection(configPath);

        if (modifierSection != null) {
            for (String key : modifierSection.getKeys(false)) {

                double modifier = modifierSection.getDouble(key);
                List<Material> materials = getBlocks(key);
                if (materials != null) {
                    for (Material material : materials) {
                        map.put(material, modifier);
                    }
                } else {
                    System.out.println("Invalid key: " + key);
                    MovecraftHitPoints.getInstance().getLogger().log(
                            Level.WARNING, "[ERROR] Invalid Material: " + ChatColor.RED + key);
                }
            }
        } else {
            System.out.println("Modifier Section is null!");
            MovecraftHitPoints.getInstance().getLogger().log(
                    Level.WARNING, configPath + " is null!");
        }
    }

    private List<Material> getBlocks(String name) {
        List<Material> blocks = new ArrayList<>();
        if (name.startsWith("minecraft:")) {
            String tagName = name.substring(10);
            Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft(tagName), Material.class);
            if (tag != null) {
                blocks.addAll(tag.getValues());
            } else {
                MovecraftHitPoints.getInstance().getLogger().log(
                        Level.WARNING, "Invalid tag: " + name);
            }
        } else {
            Material material = Material.matchMaterial(name);
            if (material != null) {
                blocks.add(material);
            }
        }
        return blocks;
    }

    public void reloadConfig() {
        Settings.BaseHitPointMultiplier = config.getDouble("BaseHitPointMultiplier", 1.0);
        Settings.BaseDamageMultiplier = config.getDouble("BaseDamageMultiplier", 1.0);
        Settings.DamageThreshold = config.getDouble("DamageThreshold", 0.99);
        Settings.CriticalThreshold = config.getDouble("CriticalThreshold", 0.3);
        Settings.TorpedoDamageMultiplier = config.getDouble("TorpedoDamageMultiplier", 50.0);

        if (config.contains("BlockDamageMultiplier")) {
            listReader("BlockDamageMultiplier", Settings.BlockDamageMultiplier);
        }

        if (config.contains("HitPointModifierBlocks")) {
            listReader("HitPointModifierBlocks", Settings.HitPointModifierBlocks);
        }
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
