package io.github.pepsidog.miniadditions.additions.craftingkeeper;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SerializableAs("CraftingKeeperManager")
public class CraftingKeeperManager implements ConfigurationSerializable {
    private Map<Location, ItemStack[]> inventories;
    private static CraftingKeeperManager self;

    private CraftingKeeperManager() {
        this.inventories = new HashMap<>();
    }

    public static CraftingKeeperManager getInstance() {
        if (self == null) {
            self = new CraftingKeeperManager();
        }
        return self;
    }

    public boolean isSaved(Location location) {
        return this.inventories.containsKey(location);
    }

    public ItemStack[] getSavedInventory(Location location) {
        return this.inventories.get(location);
    }

    public void saveInventory(Location location, ItemStack[] inventory) {
        this.inventories.put(location, inventory);
    }

    public void removeSaved(Location location) {
        this.inventories.remove(location);
    }

    private void setMap(Map<Location, ItemStack[]> invs) {
        this.inventories = invs;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        List<String> ids = new ArrayList<>();

        for (Location location : this.inventories.keySet()) {
            String randId = UUID.randomUUID().toString().replace("-", "");
            Map<String, Object> craftingTable = new HashMap<>();
            List<Map<String, Object>> contents = new ArrayList<>();

            for (ItemStack item : this.inventories.get(location)) {
                contents.add(item.serialize());
            }

            craftingTable.put("location", location.serialize());
            craftingTable.put("contents", contents);
            result.put(randId, craftingTable);
            ids.add(randId);
        }

        result.put("ids", ids);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static CraftingKeeperManager deserialize(Map<String, Object> args) {
        CraftingKeeperManager manager = getInstance();
        Map<Location, ItemStack[]> savedTables = new HashMap<>();
        List<String> ids = (ArrayList<String>) args.get("ids");

        for (String id : ids) {
            Map<String, Object> craftingTable = (Map<String, Object>) args.get(id);
            Location loc = Location.deserialize((Map<String, Object>) craftingTable.get("location"));
            List<ItemStack> items = new ArrayList<>();
            List<Map<String, Object>> craftingContents = (List<Map<String, Object>>) craftingTable.get("contents");

            craftingContents.forEach(serializedItem -> items.add(ItemStack.deserialize(serializedItem)));
            savedTables.put(loc, items.toArray(new ItemStack[items.size()]));
        }

        manager.setMap(savedTables);
        return manager;
    }
}
