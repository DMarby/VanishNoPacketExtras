package se.DMarby.VanishNoPacketExtras;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

public class VanishNoPacketExtras extends JavaPlugin implements Listener {

    private Map<String, ItemStack[]> inventories = new HashMap();
    private Map<String, ItemStack[]> armor = new HashMap();
    private Map<String, Location> cordinates = new HashMap();
    private ItemStack[] inventory;

    private void log(String message) {
        getLogger().info(message);
    }

    private void unVanish(Player player) {
        player.teleport(cordinates.get(player.getName()));
        player.getInventory().setContents(inventories.get(player.getName()));
        player.getInventory().setArmorContents(armor.get(player.getName()));
        cordinates.remove(player.getName());
        inventories.remove(player.getName());
        armor.remove(player.getName());
    }

    @Override
    public void onDisable() {
        for (String p : inventories.keySet()) {
            Player player = getServer().getPlayer(p);
            player.teleport(cordinates.get(player.getName()));
            player.getInventory().setContents(inventories.get(player.getName()));
            player.getInventory().setArmorContents(armor.get(player.getName()));
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        log("v" + getDescription().getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
        inventory = new ItemStack[]{new ItemStack(Material.getMaterial(345), 1), new ItemStack(Material.getMaterial(60), 1), new ItemStack(Material.getMaterial(270), 1), new ItemStack(Material.getMaterial(276), 1), new ItemStack(Material.getMaterial(257), 1)};
        getServer().getPluginManager().registerEvents(this, this);
        log("v" + getDescription().getVersion() + " enabled.");
    }

    @EventHandler
    public void onVanishStatusChange(VanishStatusChangeEvent event) {
        Player player = event.getPlayer();
        player.setAllowFlight(event.isVanishing());
        player.setFlying(event.isVanishing());
        if (event.isVanishing()) {
            cordinates.put(player.getName(), player.getLocation());
            inventories.put(player.getName(), player.getInventory().getContents());
            armor.put(player.getName(), player.getInventory().getArmorContents());
            player.getInventory().setArmorContents(null);
            player.getInventory().setContents(inventory);
        } else {
            unVanish(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (inventories.containsKey(event.getPlayer().getName())) {
            unVanish(event.getPlayer());
        }
    }
}