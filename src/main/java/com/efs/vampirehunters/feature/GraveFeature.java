package com.efs.vampirehunters.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class GraveFeature extends Feature<DefaultFeatureConfig> {
  public GraveFeature(Codec<DefaultFeatureConfig> configCodec) {
    super(configCodec);
  }
 
  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
    BlockPos topPos = context.getWorld().getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, context.getOrigin());
    Random rand = new Random();
    Direction offset = Direction.NORTH;
      offset = offset.rotateYClockwise();
      String playerMessage = "Steve";
      if(context.getWorld().getPlayers().size() > 0) {
       playerMessage = context.getWorld().getPlayers().get(rand.nextInt(context.getWorld().getPlayers().size())).getEntityName();}
 	 NbtCompound chestloot = new NbtCompound();
 	 chestloot.putString("LootTable", "vampirehunters:chests/graveyard");
      context.getWorld().setBlockState(topPos.offset(offset), Blocks.OAK_SIGN.getDefaultState(), 3);
      context.getWorld().setBlockState(new BlockPos(topPos.offset(offset).getX(), topPos.offset(offset).getY(), topPos.offset(offset).getZ()+1), Blocks.COBBLESTONE.getDefaultState(), 3);
      context.getWorld().setBlockState(new BlockPos(topPos.offset(offset).getX()+1, topPos.offset(offset).getY(), topPos.offset(offset).getZ()-1), Blocks.CORNFLOWER.getDefaultState(), 3);
      context.getWorld().setBlockState(new BlockPos(topPos.offset(offset).getX()-1, topPos.offset(offset).getY(), topPos.offset(offset).getZ()-1), Blocks.CORNFLOWER.getDefaultState(), 3);
      context.getWorld().setBlockState(new BlockPos(topPos.offset(offset).getX(), topPos.offset(offset).getY()-3, topPos.offset(offset).getZ()-1), Blocks.CHEST.getDefaultState(), 3);
      context.getWorld().getBlockEntity(new BlockPos(topPos.offset(offset).getX(), topPos.offset(offset).getY()-3, topPos.offset(offset).getZ()-1)).writeNbt(chestloot);
      context.getWorld().getBlockEntity(topPos.offset(offset), BlockEntityType.SIGN).get().setTextOnRow(1, Text.of(playerMessage + " Tried to"));
      context.getWorld().getBlockEntity(topPos.offset(offset), BlockEntityType.SIGN).get().setTextOnRow(2, Text.of(playerMessage + "Swim in Lava"));
    return true;
  }
}