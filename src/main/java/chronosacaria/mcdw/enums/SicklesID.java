package chronosacaria.mcdw.enums;

import chronosacaria.mcdw.Mcdw;
import chronosacaria.mcdw.api.interfaces.IInnateEnchantment;
import chronosacaria.mcdw.bases.McdwSickle;
import chronosacaria.mcdw.configs.McdwNewStatsConfig;
import chronosacaria.mcdw.registries.EnchantsRegistry;
import chronosacaria.mcdw.registries.ItemsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static chronosacaria.mcdw.Mcdw.CONFIG;

public enum SicklesID implements IMeleeWeaponID, IInnateEnchantment {
    SICKLE_LAST_LAUGH_GOLD(ToolMaterials.IRON,2, -2.1f, "minecraft:iron_ingot"),
    SICKLE_LAST_LAUGH_SILVER(ToolMaterials.IRON,2, -2.1f, "minecraft:iron_ingot"),
    SICKLE_NIGHTMARES_BITE(ToolMaterials.IRON,2, -2.1f, "minecraft:iron_ingot"),
    SICKLE_SICKLE(ToolMaterials.IRON,1, -2.1f, "minecraft:iron_ingot");

    private final ToolMaterial material;
    private final int damage;
    private final float attackSpeed;
    private final String[] repairIngredient;

    SicklesID(ToolMaterial material, int damage, float attackSpeed, String... repairIngredient) {
        this.material = material;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.repairIngredient = repairIngredient;
    }

    public static HashMap<SicklesID, Boolean> getEnabledItems(){
        return Mcdw.CONFIG.mcdwEnableItemsConfig.SICKLES_ENABLED;
    }

    @SuppressWarnings("SameReturnValue")
    public static EnumMap<SicklesID, McdwSickle> getItemsEnum() {
        return ItemsRegistry.SICKLE_ITEMS;
    }

    public static HashMap<SicklesID, Integer> getSpawnRates() {
        return Mcdw.CONFIG.mcdwNewlootConfig.SICKLE_SPAWN_RATES;
    }

    public static HashMap<SicklesID, MeleeStats> getWeaponStats() {
        return CONFIG.mcdwNewStatsConfig.sickleStats;
    }

    @Override
    public Boolean isEnabled(){
        return getEnabledItems().get(this);
    }

    @Override
    public McdwSickle getItem() {
        return getItemsEnum().get(this);
    }

    @Override
    public Integer getItemSpawnRate() {
        return getSpawnRates().get(this);
    }

    @Override
    public HashMap<SicklesID, MeleeStats> getWeaponStats(McdwNewStatsConfig mcdwNewStatsConfig) {
        return mcdwNewStatsConfig.sickleStats;
    }

    @Override
    public MeleeStats getWeaponItemStats() {
        return getWeaponStats().get(this);
    }

    @Override
    public MeleeStats getWeaponItemStats(McdwNewStatsConfig mcdwNewStatsConfig) {
        return mcdwNewStatsConfig.sickleStats.get(this);
    }

    @Override
    public ToolMaterial getMaterial(){
        return material;
    }

    @Override
    public int getDamage(){
        return damage;
    }

    @Override
    public float getAttackSpeed(){
        return attackSpeed;
    }

    @Override
    public String[] getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public Map<Enchantment, Integer> getInnateEnchantments() {
        return switch (this) {
            case SICKLE_LAST_LAUGH_GOLD, SICKLE_LAST_LAUGH_SILVER -> Map.of(EnchantsRegistry.PROSPECTOR, 1);
            case SICKLE_NIGHTMARES_BITE -> Map.of(EnchantsRegistry.POISON_CLOUD, 1);
            case SICKLE_SICKLE -> null;
        };
    }

    @Override
    public @NotNull ItemStack getInnateEnchantedStack(Item item) {
        return item.getDefaultStack();
    }

    @Override
    public McdwSickle makeWeapon() {
        McdwSickle mcdwSickle = new McdwSickle(this, ItemsRegistry.stringToMaterial(this.getWeaponItemStats().material),
                this.getWeaponItemStats().damage, this.getWeaponItemStats().attackSpeed, this.getWeaponItemStats().repairIngredient);

        getItemsEnum().put(this, mcdwSickle);
        return mcdwSickle;
    }
}