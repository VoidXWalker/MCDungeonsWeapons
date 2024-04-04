package chronosacaria.mcdw.mixin.mcdw.client;

import chronosacaria.mcdw.api.interfaces.IDualWielding;
import chronosacaria.mcdw.api.interfaces.IOffhandAttack;
import chronosacaria.mcdw.api.util.PlayerAttackHelper;
import chronosacaria.mcdw.configs.CompatibilityFlags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Shadow @Final @Mutable
    private MinecraftClient client;

    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Shadow @Final private static Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;

    @Shadow @Final private static Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE;

    @Shadow @Final private static Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE;

    public InGameHudMixin(MinecraftClient client) {
        this.client = client;
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F", shift = At.Shift.AFTER))
    private void renderOffhandCrosshair(DrawContext context, CallbackInfo ci) {
        if (CompatibilityFlags.noOffhandConflicts) {
            PlayerEntity player = client.player;
            if (player == null)
                return;
            if (player.getOffHandStack().getItem() instanceof IOffhandAttack && PlayerAttackHelper.mixAndMatchWeapons(player)) {

                GameOptions gameOptions = this.client.options;
                if (gameOptions.getPerspective().isFirstPerson()) {
                    if (this.client.interactionManager != null) {
                        if (this.client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR || mcdw$shouldRenderSpectatorCrosshair(this.client.crosshairTarget)) {
                            if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
                                PlayerAttackHelper.mcdw$switchModifiers(player, player.getMainHandStack(), player.getOffHandStack());
                                float offhandAttackCooldownProgress = ((IDualWielding) player).mcdw$getOffhandAttackCooldownProgress(0.0f);
                                boolean bl = false;
                                if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && offhandAttackCooldownProgress >= 1.0f) {
                                    bl = ((IDualWielding) player).mcdw$getOffhandAttackCooldownProgressPerTick() > 5.0f;
                                    bl &= this.client.targetedEntity.isAlive();
                                }
                                PlayerAttackHelper.mcdw$switchModifiers(player, player.getOffHandStack(), player.getMainHandStack());
                                int height = this.scaledHeight / 2 - 7 + 16;
                                int width = this.scaledWidth / 2 - 8;
                                if (bl) {
                                    context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_FULL_TEXTURE, width, height+8, 16, 16);
                                } else if (offhandAttackCooldownProgress < 1.0F) {
                                    int l = (int)(offhandAttackCooldownProgress * 17.0F);
                                    context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, width, height+8, 16, 4);
                                    context.drawGuiTexture(CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, width, height +8 , l, 4);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Unique
    private boolean mcdw$shouldRenderSpectatorCrosshair(HitResult hitResult) {
        if (hitResult == null) {
            return false;
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult)hitResult).getEntity() instanceof NamedScreenHandlerFactory;
        } else if (hitResult.getType() == HitResult.Type.BLOCK && this.client.world != null) {
            BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
            World world = this.client.world;
            return world.getBlockState(blockPos).createScreenHandlerFactory(world, blockPos) != null;
        } else {
            return false;
        }
    }
}
