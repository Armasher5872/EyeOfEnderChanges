package net.phazoganon.eyeofenderrework.mixin.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.phazoganon.eyeofenderrework.util.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeMixin extends Item {
    public EnderEyeMixin(Properties properties) {
        super(properties);
    }
    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    private void use(Level p_41184_, Player p_41185_, InteractionHand p_41186_, CallbackInfoReturnable<InteractionResult> cir) {
        cir.cancel();
        ItemStack itemstack = p_41185_.getItemInHand(p_41186_);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(p_41184_, p_41185_, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.BLOCK && p_41184_.getBlockState(blockhitresult.getBlockPos()).is(Blocks.END_PORTAL_FRAME)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
        else {
            p_41185_.startUsingItem(p_41186_);
            if (p_41184_ instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel) p_41184_;
                BlockPos blockpos = null;
                if (p_41184_.dimension() == Level.NETHER) {
                    blockpos = serverlevel.findNearestMapStructure(ModTags.Structures.BASTION, p_41185_.blockPosition(), 1, false);
                }
                if (p_41184_.dimension() == Level.END) {
                    blockpos = serverlevel.findNearestMapStructure(ModTags.Structures.END_CITIES, p_41185_.blockPosition(), 1, false);
                }
                if (p_41184_.dimension() == Level.OVERWORLD) {
                    blockpos = serverlevel.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, p_41185_.blockPosition(), 1, false);
                }
                if (blockpos == null) {
                    cir.setReturnValue(InteractionResult.CONSUME);
                }
                EyeOfEnder eyeofender = new EyeOfEnder(p_41184_, p_41185_.getX(), p_41185_.getY(0.5), p_41185_.getZ());
                eyeofender.setItem(itemstack);
                eyeofender.signalTo(blockpos);
                p_41184_.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeofender.position(), GameEvent.Context.of(p_41185_));
                p_41184_.addFreshEntity(eyeofender);
                if (p_41185_ instanceof ServerPlayer) {
                    ServerPlayer serverplayer = (ServerPlayer)p_41185_;
                    CriteriaTriggers.USED_ENDER_EYE.trigger(serverplayer, blockpos);
                }
                float f = Mth.lerp(p_41184_.random.nextFloat(), 0.33F, 0.5F);
                p_41184_.playSound((Player)null, p_41185_.getX(), p_41185_.getY(), p_41185_.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, f);
                itemstack.consume(1, p_41185_);
                p_41185_.awardStat(Stats.ITEM_USED.get(this));
            }
            cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
        }
    }
}
