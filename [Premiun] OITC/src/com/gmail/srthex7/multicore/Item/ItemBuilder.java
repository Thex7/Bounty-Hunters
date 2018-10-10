package com.gmail.srthex7.multicore.Item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import com.google.gson.Gson;

public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    private Material material = Material.STONE;
    private int amount = 1;
    private MaterialData data;
    private short damage = 0;
    private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
    private String displayname;
    private List<String> lore = new ArrayList<String>();
    private boolean andSymbol = true;
    private boolean unsafeStackSize = false;

    public ItemBuilder(Material material) {
        if (material == null) {
            material = Material.AIR;
        }
        this.item = new ItemStack(material);
        this.material = material;
    }

    public ItemBuilder(Material material, int amount) {
        if (material == null) {
            material = Material.AIR;
        }
        if (!(amount <= material.getMaxStackSize() && amount > 0 || this.unsafeStackSize)) {
            amount = 1;
        }
        this.amount = amount;
        this.item = new ItemStack(material, amount);
        this.material = material;
    }

    public ItemBuilder(Material material, int amount, String displayname) {
        if (material == null) {
            material = Material.AIR;
        }
        Validate.notNull((Object)displayname, (String)"The Displayname is null.");
        this.item = new ItemStack(material, amount);
        this.material = material;
        if (!(amount <= material.getMaxStackSize() && amount > 0 || this.unsafeStackSize)) {
            amount = 1;
        }
        this.amount = amount;
        this.displayname = displayname;
    }

    public ItemBuilder(Material material, String displayname) {
        if (material == null) {
            material = Material.AIR;
        }
        Validate.notNull((Object)displayname, (String)"The Displayname is null.");
        this.item = new ItemStack(material);
        this.material = material;
        this.displayname = displayname;
    }

    public ItemBuilder(ItemStack item) {
        Validate.notNull((Object)item, (String)"The Item is null.");
        this.item = item;
        if (item.hasItemMeta()) {
            this.meta = item.getItemMeta();
        }
        this.material = item.getType();
        this.amount = item.getAmount();
        this.data = item.getData();
        this.damage = item.getDurability();
        this.enchantments = item.getEnchantments();
        if (item.hasItemMeta()) {
            this.displayname = item.getItemMeta().getDisplayName();
        }
        if (item.hasItemMeta()) {
            this.lore = item.getItemMeta().getLore();
        }
    }

    public ItemBuilder(FileConfiguration cfg, String path) {
        this(cfg.getItemStack(path));
    }

    @Deprecated
    public ItemBuilder(ItemBuilder builder) {
        Validate.notNull((Object)builder, (String)"The ItemBuilder is null.");
        this.item = builder.item;
        this.meta = builder.meta;
        this.material = builder.material;
        this.amount = builder.amount;
        this.damage = builder.damage;
        this.data = builder.data;
        this.damage = builder.damage;
        this.enchantments = builder.enchantments;
        this.displayname = builder.displayname;
        this.lore = builder.lore;
    }

    public ItemBuilder amount(int amount) {
        if (!(amount <= this.material.getMaxStackSize() && amount > 0 || this.unsafeStackSize)) {
            amount = 1;
        }
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(MaterialData data) {
        Validate.notNull((Object)data, (String)"The Data is null.");
        this.data = data;
        return this;
    }

    @Deprecated
    public ItemBuilder damage(short damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder durability(short damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder material(Material material) {
        Validate.notNull((Object)material, (String)"The Material is null.");
        this.material = material;
        return this;
    }

    public ItemBuilder meta(ItemMeta meta) {
        Validate.notNull((Object)meta, (String)"The Meta is null.");
        this.meta = meta;
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        Validate.notNull((Object)enchant, (String)"The Enchantment is null.");
        this.enchantments.put(enchant, level);
        return this;
    }

    public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, (String)"The Enchantments are null.");
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder displayname(String displayname) {
        Validate.notNull((Object)displayname, (String)"The Displayname is null.");
        this.displayname = this.andSymbol ? ChatColor.translateAlternateColorCodes((char)'&', (String)displayname) : displayname;
        return this;
    }

    public ItemBuilder lore(String line) {
        Validate.notNull((Object)line, (String)"The Line is null.");
        this.lore.add(this.andSymbol ? ChatColor.translateAlternateColorCodes((char)'&', (String)line) : line);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        Validate.notNull(lore, (String)"The Lores are null.");
        this.lore = lore;
        return this;
    }

    @Deprecated
    public ItemBuilder lores(String ... lines) {
        Validate.notNull((Object)lines, (String)"The Lines are null.");
        String[] arrstring = lines;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            this.lore(this.andSymbol ? ChatColor.translateAlternateColorCodes((char)'&', (String)line) : line);
            ++n2;
        }
        return this;
    }

    public ItemBuilder lore(String ... lines) {
        Validate.notNull((Object)lines, (String)"The Lines are null.");
        String[] arrstring = lines;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            this.lore(this.andSymbol ? ChatColor.translateAlternateColorCodes((char)'&', (String)line) : line);
            ++n2;
        }
        return this;
    }

    public ItemBuilder lore(String line, int index) {
        Validate.notNull((Object)line, (String)"The Line is null.");
        this.lore.set(index, this.andSymbol ? ChatColor.translateAlternateColorCodes((char)'&', (String)line) : line);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.meta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    @Deprecated
    public ItemBuilder owner(String user) {
        Validate.notNull((Object)user, (String)"The Username is null.");
        if (this.material == Material.SKULL_ITEM || this.material == Material.SKULL) {
            SkullMeta smeta = (SkullMeta)this.meta;
            smeta.setOwner(user);
            this.meta = smeta;
        }
        return this;
    }

    public Unsafe unsafe() {
        return new Unsafe(this);
    }

    @Deprecated
    public ItemBuilder replaceAndSymbol() {
        this.replaceAndSymbol(!this.andSymbol);
        return this;
    }

    public ItemBuilder replaceAndSymbol(boolean replace) {
        this.andSymbol = replace;
        return this;
    }

    public ItemBuilder toggleReplaceAndSymbol() {
        this.replaceAndSymbol(!this.andSymbol);
        return this;
    }

    public ItemBuilder unsafeStackSize(boolean allow) {
        this.unsafeStackSize = allow;
        return this;
    }

    public ItemBuilder toggleUnsafeStackSize() {
        this.unsafeStackSize(!this.unsafeStackSize);
        return this;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public int getAmount() {
        return this.amount;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    @Deprecated
    public short getDamage() {
        return this.damage;
    }

    public short getDurability() {
        return this.damage;
    }

    public List<String> getLores() {
        return this.lore;
    }

    public boolean getAndSymbol() {
        return this.andSymbol;
    }

    public Material getMaterial() {
        return this.material;
    }

    public ItemMeta getMeta() {
        return this.meta;
    }

    public MaterialData getData() {
        return this.data;
    }

    @Deprecated
    public List<String> getLore() {
        return this.lore;
    }

    public ItemBuilder toConfig(FileConfiguration cfg, String path) {
        cfg.set(path, (Object)this.build());
        return this;
    }

    public ItemBuilder fromConfig(FileConfiguration cfg, String path) {
        return new ItemBuilder(cfg, path);
    }

    public static void toConfig(FileConfiguration cfg, String path, ItemBuilder builder) {
        cfg.set(path, (Object)builder.build());
    }

    public String toJson() {
        return new Gson().toJson((Object)this);
    }

    public static String toJson(ItemBuilder builder) {
        return new Gson().toJson((Object)builder);
    }

    public static ItemBuilder fromJson(String json) {
        return (ItemBuilder)new Gson().fromJson(json, (Class)ItemBuilder.class);
    }

    public ItemBuilder applyJson(String json, boolean overwrite) {
        ItemBuilder b = (ItemBuilder)new Gson().fromJson(json, (Class)ItemBuilder.class);
        if (overwrite) {
            return b;
        }
        if (b.displayname != null) {
            this.displayname = b.displayname;
        }
        if (b.data != null) {
            this.data = b.data;
        }
        if (b.material != null) {
            this.material = b.material;
        }
        if (b.lore != null) {
            this.lore = b.lore;
        }
        if (b.enchantments != null) {
            this.enchantments = b.enchantments;
        }
        if (b.item != null) {
            this.item = b.item;
        }
        this.damage = b.damage;
        this.amount = b.amount;
        return this;
    }

    public ItemStack build() {
        this.item.setType(this.material);
        this.item.setAmount(this.amount);
        this.item.setDurability(this.damage);
        this.meta = this.item.getItemMeta();
        if (this.data != null) {
            this.item.setData(this.data);
        }
        if (this.enchantments.size() > 0) {
            this.item.addUnsafeEnchantments(this.enchantments);
        }
        if (this.displayname != null) {
            this.meta.setDisplayName(this.displayname);
        }
        if (this.lore.size() > 0) {
            this.meta.setLore(this.lore);
        }
        this.item.setItemMeta(this.meta);
        return this.item;
    }

    static void access1(ItemBuilder itemBuilder, ItemStack itemStack) {
        itemBuilder.item = itemStack;
    }

    public class Unsafe {
        protected final ReflectionUtils utils;
        protected final ItemBuilder builder;

        public Unsafe(ItemBuilder builder) {
            this.utils = new ReflectionUtils();
            this.builder = builder;
        }

        public Unsafe setString(String key, String value) {
            ItemBuilder.access1(this.builder, this.utils.setString(this.builder.item, key, value));
            return this;
        }

        public String getString(String key) {
            return this.utils.getString(this.builder.item, key);
        }

        public Unsafe setInt(String key, int value) {
            ItemBuilder.access1(this.builder, this.utils.setInt(this.builder.item, key, value));
            return this;
        }

        public int getInt(String key) {
            return this.utils.getInt(this.builder.item, key);
        }

        public Unsafe setDouble(String key, double value) {
            ItemBuilder.access1(this.builder, this.utils.setDouble(this.builder.item, key, value));
            return this;
        }

        public double getDouble(String key) {
            return this.utils.getDouble(this.builder.item, key);
        }

        public Unsafe setBoolean(String key, boolean value) {
            ItemBuilder.access1(this.builder, this.utils.setBoolean(this.builder.item, key, value));
            return this;
        }

        public boolean getBoolean(String key) {
            return this.utils.getBoolean(this.builder.item, key);
        }

        public boolean containsKey(String key) {
            return this.utils.hasKey(this.builder.item, key);
        }

        public ItemBuilder builder() {
            return this.builder;
        }

        public class ReflectionUtils {
            public String getString(ItemStack item, String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (String)compound.getClass().getMethod("getString", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            public ItemStack setString(ItemStack item, String key, String value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setString", String.class, String.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }

            public int getInt(ItemStack item, String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (Integer)compound.getClass().getMethod("getInt", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return -1;
                }
            }

            public ItemStack setInt(ItemStack item, String key, int value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setInt", String.class, Integer.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }

            public double getDouble(ItemStack item, String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (Double)compound.getClass().getMethod("getDouble", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return Double.NaN;
                }
            }

            public ItemStack setDouble(ItemStack item, String key, double value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setDouble", String.class, Double.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }

            public boolean getBoolean(ItemStack item, String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (Boolean)compound.getClass().getMethod("getBoolean", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            public ItemStack setBoolean(ItemStack item, String key, boolean value) {
                Object nmsItem = this.getItemAsNMSStack(item);
                Object compound = this.getNBTTagCompound(nmsItem);
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    compound.getClass().getMethod("setBoolean", String.class, Boolean.class).invoke(compound, key, value);
                    nmsItem = this.setNBTTag(compound, nmsItem);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                return this.getItemAsBukkitStack(nmsItem);
            }

            public boolean hasKey(ItemStack item, String key) {
                Object compound = this.getNBTTagCompound(this.getItemAsNMSStack(item));
                if (compound == null) {
                    compound = this.getNewNBTTagCompound();
                }
                try {
                    return (Boolean)compound.getClass().getMethod("hasKey", String.class).invoke(compound, key);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            public Object getNewNBTTagCompound() {
                String ver = Bukkit.getServer().getClass().getPackage().getName().split(".")[3];
                try {
                    return Class.forName("net.minecraft.server." + ver + ".NBTTagCompound").newInstance();
                }
                catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            public Object setNBTTag(Object tag, Object item) {
                try {
                    item.getClass().getMethod("setTag", item.getClass()).invoke(item, tag);
                    return item;
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            public Object getNBTTagCompound(Object nmsStack) {
                try {
                    return nmsStack.getClass().getMethod("getTag", new Class[0]).invoke(nmsStack, new Object[0]);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            public Object getItemAsNMSStack(ItemStack item) {
                try {
                    Method m = this.getCraftItemStackClass().getMethod("asNMSCopy", ItemStack.class);
                    return m.invoke(this.getCraftItemStackClass(), new Object[]{item});
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            public ItemStack getItemAsBukkitStack(Object nmsStack) {
                try {
                    Method m = this.getCraftItemStackClass().getMethod("asCraftMirror", nmsStack.getClass());
                    return (ItemStack)m.invoke(this.getCraftItemStackClass(), nmsStack);
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            public Class<?> getCraftItemStackClass() {
                String ver = Bukkit.getServer().getClass().getPackage().getName().split(".")[3];
                try {
                    return Class.forName("org.bukkit.craftbukkit." + ver + ".inventory.CraftItemStack");
                }
                catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        }

    }

}

