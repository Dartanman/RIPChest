package me.dartanman.ripchest.listeners.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class DeathKeyUtils {
	
	public static ItemStack createDeathKey()
	{
		ItemStack deathKey = new ItemStack(Material.STICK);
		ItemMeta keyMeta = deathKey.getItemMeta();
		
		keyMeta.setDisplayName(ChatColor.DARK_PURPLE + "DEATH KEY");
		List<String> lore = new ArrayList();
		lore.add("Use this key to open another player's death chest");
		keyMeta.setLore(lore);
		keyMeta.addEnchant(Enchantment.MENDING, 0, true);
		keyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		deathKey.setItemMeta(keyMeta);
		
		return deathKey;
	}
	
	public static void registerCraftingRecipe()
	{
		NamespacedKey craftingKey = new NamespacedKey(Bukkit.getPluginManager().getPlugin("RIPChest"), "death_key");
		if(Bukkit.getServer().getRecipe(craftingKey) != null)
		{
			Bukkit.getServer().removeRecipe(craftingKey);
		}
		ShapelessRecipe recipe = new ShapelessRecipe(craftingKey, createDeathKey());
		recipe.addIngredient(Material.EMERALD);
		recipe.addIngredient(Material.TOTEM_OF_UNDYING);
		Bukkit.getServer().addRecipe(recipe);
	}

}
