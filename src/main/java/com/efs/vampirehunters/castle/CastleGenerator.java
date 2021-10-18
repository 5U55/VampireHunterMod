package com.efs.vampirehunters.castle;

import java.util.Random;

import com.efs.vampirehunters.VampireHunters;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class CastleGenerator {
	static final BlockPos DEFAULT_POSITION = new BlockPos(4, 0, 15);
	private static final Identifier[] REGULAR_TEMPLATES = new Identifier[] {
			new Identifier("vampirehunters", "castle/draculas_castle1"),
			new Identifier("vampirehunters", "castle/draculas_castle2"),
			new Identifier("vampirehunters", "castle/draculas_castle3"),
			new Identifier("vampirehunters", "castle/draculas_castle4") };

	public static void addParts(StructureManager structureManager, BlockPos pos, BlockRotation rotation,
			StructurePiecesHolder structurePiecesHolder, Random random, DefaultFeatureConfig config) {
		structurePiecesHolder
				.addPiece(new CastleGenerator.Piece(structureManager, new Identifier("vampirehunters", "castle/draculas_castle1"), new BlockPos(pos.getX(), pos.getY(), pos.getZ()-27), rotation, false));
		structurePiecesHolder
				.addPiece(new CastleGenerator.Piece(structureManager, new Identifier("vampirehunters", "castle/draculas_castle2"), new BlockPos(pos.getX()-32, pos.getY(), pos.getZ()-27), rotation, false));
		structurePiecesHolder
				.addPiece(new CastleGenerator.Piece(structureManager, new Identifier("vampirehunters", "castle/draculas_castle3"), new BlockPos(pos.getX()-32, pos.getY(), pos.getZ()), rotation, false));
		structurePiecesHolder
				.addPiece(new CastleGenerator.Piece(structureManager, new Identifier("vampirehunters", "castle/draculas_castle4"), new BlockPos(pos.getX(), pos.getY(), pos.getZ()), rotation, false));
	}

	public static class Piece extends SimpleStructurePiece {

		public Piece(StructureManager manager, Identifier identifier, BlockPos pos, BlockRotation rotation,
				boolean grounded) {
			super(VampireHunters.CASTLE_PIECE, 0, manager, identifier, identifier.toString(),
					createPlacementData(rotation), pos);
		}

		public Piece(ServerWorld world, NbtCompound nbt) {
			super(VampireHunters.CASTLE_PIECE, nbt, world, (identifier) -> {
				return createPlacementData(BlockRotation.valueOf(nbt.getString("Rot")));
			});
		}

		protected void writeNbt(ServerWorld world, NbtCompound nbt) {
			super.writeNbt(world, nbt);
			nbt.putString("Rot", this.placementData.getRotation().name());
		}

		private static StructurePlacementData createPlacementData(BlockRotation rotation) {
			return (new StructurePlacementData()).setRotation(rotation).setMirror(BlockMirror.NONE)
					.setPosition(CastleGenerator.DEFAULT_POSITION)
					.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
		}

		@Override
		protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random,
				BlockBox boundingBox) {
		}
	}
}
