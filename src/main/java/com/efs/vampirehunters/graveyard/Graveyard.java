package com.efs.vampirehunters.graveyard;

import com.mojang.serialization.Codec;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class Graveyard extends StructureFeature<DefaultFeatureConfig> {
	public Graveyard(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
		return Graveyard.Start::new;
	}

	public static class Start extends StructureStart<DefaultFeatureConfig> {
		public Start(StructureFeature<DefaultFeatureConfig> structureFeature, ChunkPos chunkPos, int i, long l) {
			super(structureFeature, chunkPos, i, l);
		}

		public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator,
				StructureManager structureManager, ChunkPos chunkPos, Biome biome, DefaultFeatureConfig FeatureConfig,
				HeightLimitView heightLimitView) {
			BlockRotation blockRotation = BlockRotation.random(this.random);
			int i = chunkGenerator.getHeight(chunkPos.getStartX(), chunkPos.getStartZ(), Heightmap.Type.WORLD_SURFACE,
					heightLimitView);
			BlockPos blockPos = new BlockPos(chunkPos.getStartX(), i, chunkPos.getStartZ());
			GraveyardGenerator.addParts(structureManager, blockPos, blockRotation, this, this.random, FeatureConfig);
		}
	}
}