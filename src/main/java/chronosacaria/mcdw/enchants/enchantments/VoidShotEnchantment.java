package chronosacaria.mcdw.enchants.enchantments;

import chronosacaria.mcdw.Mcdw;
import chronosacaria.mcdw.enchants.types.AOEEnchantment;
import chronosacaria.mcdw.enchants.types.DamageBoostEnchantment;
import chronosacaria.mcdw.enums.EnchantmentsID;
import chronosacaria.mcdw.enums.SettingsID;
import chronosacaria.mcdw.registries.ItemGroupRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class VoidShotEnchantment extends DamageBoostEnchantment {

    public VoidShotEnchantment(Rarity rarity, EnchantmentTarget enchantmentTarget, EquipmentSlot[] equipmentSlots) {
        super(rarity, enchantmentTarget, equipmentSlots);
        if (Mcdw.CONFIG.mcdwEnchantmentsConfig.ENABLE_ENCHANTMENTS.get(EnchantmentsID.VOID_SHOT)) {
            Registry.register(Registries.ENCHANTMENT, Mcdw.ID("void_shot"), this);
            ItemGroupEvents.modifyEntriesEvent(ItemGroupRegistry.ENCHANTMENTS).register(entries -> {
                // For loop creates first 3 levels of enchanted books
                for (int i = 1; i <= getMaxLevel(); i++)
                    entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(this, i)));
            });
        }
    }

    @Override
    public int getMaxLevel(){
        return 3;
    }

    @Override
    protected boolean canAccept (Enchantment other){
        return Mcdw.CONFIG.mcdwEnchantmentSettingsConfig.ENABLE_ENCHANTMENT_SETTINGS.get(SettingsID.ENABLE_OP_ENCHANTMENT_MIXING)
                || !(other instanceof AOEEnchantment || other instanceof DamageBoostEnchantment);
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return Mcdw.CONFIG.mcdwEnchantmentsConfig.ENABLE_ENCHANTMENTS.get(EnchantmentsID.VOID_SHOT)
                && Mcdw.CONFIG.mcdwEnchantmentsConfig.ENABLE_RANDOM_SELECTION.get(EnchantmentsID.VOID_SHOT);
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return Mcdw.CONFIG.mcdwEnchantmentsConfig.ENABLE_ENCHANTMENTS.get(EnchantmentsID.VOID_SHOT)
                && Mcdw.CONFIG.mcdwEnchantmentsConfig.ENABLE_VILLAGER_TRADING.get(EnchantmentsID.VOID_SHOT);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem || stack.getItem() instanceof BowItem;
    }

}

