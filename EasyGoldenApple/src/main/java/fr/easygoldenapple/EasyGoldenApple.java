package fr.easygoldenapple;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyGoldenApple : mode "easycraft" pour les pommes d'or.
 *
 *  - Pomme d'or           : 4 lingots d'or + 1 pomme, en croix (pomme au centre)
 *  - Pomme d'or enchantée : 4 blocs d'or   + 1 pomme, en croix (optionnelle)
 *
 *        . G .
 *        G A G
 *        . G .
 */
public final class EasyGoldenApple extends JavaPlugin implements Listener {

    // ---------------------------------------------------------------
    //  CONFIGURATION
    // ---------------------------------------------------------------
    private static final boolean ENABLE_ENCHANTED_RECIPE = false;   // true = active la notch apple
    private static final boolean UNLOCK_ON_JOIN = true;             // débloque les recettes à la connexion

    // ---------------------------------------------------------------
    //  DONNÉES
    // ---------------------------------------------------------------
    private final List<NamespacedKey> keys = new ArrayList<>();

    // ---------------------------------------------------------------
    //  CYCLE DE VIE
    // ---------------------------------------------------------------
    @Override
    public void onEnable() {
        registerRecipe("easy_golden_apple", Material.GOLDEN_APPLE, Material.GOLD_INGOT);
        if (ENABLE_ENCHANTED_RECIPE) {
            registerRecipe("easy_enchanted_golden_apple", Material.ENCHANTED_GOLDEN_APPLE, Material.GOLD_BLOCK);
        }
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info(keys.size() + " recette(s) enregistrée(s).");
    }

    @Override
    public void onDisable() {
        keys.forEach(Bukkit::removeRecipe);
        keys.clear();
    }

    // ---------------------------------------------------------------
    //  RECETTES
    // ---------------------------------------------------------------
    private void registerRecipe(String id, Material result, Material gold) {
        NamespacedKey key = new NamespacedKey(this, id);
        ShapedRecipe recipe = new ShapedRecipe(key, new ItemStack(result));
        recipe.shape(" G ", "GAG", " G ");
        recipe.setIngredient('G', gold);
        recipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(recipe);
        keys.add(key);
    }

    // ---------------------------------------------------------------
    //  ÉVÉNEMENTS
    // ---------------------------------------------------------------
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (UNLOCK_ON_JOIN) event.getPlayer().discoverRecipes(keys);
    }
}
