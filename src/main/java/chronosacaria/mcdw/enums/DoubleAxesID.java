package chronosacaria.mcdw.enums;

import chronosacaria.mcdw.Mcdw;
import chronosacaria.mcdw.bases.McdwDoubleAxe;
import chronosacaria.mcdw.configs.McdwNewStatsConfig;
import chronosacaria.mcdw.configs.stats.MeleeWeaponStats;
import chronosacaria.mcdw.items.ItemsInit;
import net.minecraft.item.ToolMaterials;

import java.util.EnumMap;
import java.util.HashMap;

import static chronosacaria.mcdw.Mcdw.CONFIG;

public enum DoubleAxesID implements IMcdwWeaponID, IMeleeWeaponID {
    AXE_CURSED(McdwNewStatsConfig.materialToString(ToolMaterials.IRON),7, -3.1f),
    AXE_DOUBLE(McdwNewStatsConfig.materialToString(ToolMaterials.IRON),6, -3.1f),
    AXE_WHIRLWIND(McdwNewStatsConfig.materialToString(ToolMaterials.IRON),6, -2.9f);

    private final String material;
    private final int damage;
    private final float attackSpeed;

    DoubleAxesID(String material, int damage, float attackSpeed) {
        this.material = material;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }

    public static HashMap<DoubleAxesID, Boolean> getEnabledItems(){
        return Mcdw.CONFIG.mcdwEnableItemsConfig.doubleAxesEnabled;
    }

    public static EnumMap<DoubleAxesID, McdwDoubleAxe> getItemsEnum() {
        return ItemsInit.doubleAxeItems;
    }

    public static HashMap<DoubleAxesID, Float> getSpawnRates() {
        return Mcdw.CONFIG.mcdwNewlootConfig.doubleAxeSpawnRates;
    }

    public static HashMap<IMeleeWeaponID, MeleeWeaponStats> getWeaponStats() {
        return CONFIG.mcdwNewStatsConfig.doubleAxeStats;
    }

    public Boolean isEnabled(){
        return getEnabledItems().get(this);
    }

    @Override
    public McdwDoubleAxe getItem() {
        return getItemsEnum().get(this);
    }

    @Override
    public Float getItemSpawnRate() {
        return getSpawnRates().get(this);
    }

    @Override
    public HashMap<IMeleeWeaponID, MeleeWeaponStats> getWeaponStats(McdwNewStatsConfig mcdwNewStatsConfig) {
        return mcdwNewStatsConfig.doubleAxeStats;
    }

    public MeleeWeaponStats getWeaponItemStats() {
        return getWeaponStats().get(this);
    }

    @Override
    public MeleeWeaponStats getWeaponItemStats(McdwNewStatsConfig mcdwNewStatsConfig) {
        return mcdwNewStatsConfig.doubleAxeStats.get(this);
    }

    public int getDamage(){
        return damage;
    }

    public String getMaterial(){
        return material;
    }

    public float getAttackSpeed(){
        return attackSpeed;
    }

    @Override
    public McdwDoubleAxe makeWeapon() {
        McdwDoubleAxe mcdwDoubleAxe = new McdwDoubleAxe(ItemsInit.stringToMaterial(this.getWeaponItemStats().material),
                this.getWeaponItemStats().damage, this.getWeaponItemStats().attackSpeed);

        getItemsEnum().put(this, mcdwDoubleAxe);
        return mcdwDoubleAxe;
    }
}