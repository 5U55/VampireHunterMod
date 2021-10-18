package com.efs.vampirehunters.bossmob;

import com.efs.vampirehunters.VampireHunters;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class VampireBlock extends Block {	

   /**
    * Creates an infested block
    * 
    * @param regularBlock the block this infested block should mimic
    * @param settings block settings
    */
   public VampireBlock(AbstractBlock.Settings settings) {
	   super(settings);
   }


   private void spawnVampire(ServerWorld world, BlockPos pos) {
      VampireEntity VampireEntity = (VampireEntity)VampireHunters.VAMPIRE.create(world);
      VampireEntity.refreshPositionAndAngles((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
      world.spawnEntity(VampireEntity);
      VampireEntity.playSpawnEffects();
   }

   public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
      super.onStacksDropped(state, world, pos, stack);
      if (world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
         this.spawnVampire(world, pos);
      }

   }

   public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
      if (world instanceof ServerWorld) {
         this.spawnVampire((ServerWorld)world, pos);
      }

   }
}
