package com.efs.vampirehunters.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class GraveBlock extends Block {
    protected static final VoxelShape GRAVE_SHAPE;
 
    public GraveBlock(Settings settings) {
        super(settings);
    }
    
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return GRAVE_SHAPE;
     }
    
    static {
        GRAVE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
     }
}