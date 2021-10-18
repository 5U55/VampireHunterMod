package com.efs.vampirehunters;

import com.efs.vampirehunters.bossmob.VampireBlock;
import com.efs.vampirehunters.bossmob.VampireEntity;
import com.efs.vampirehunters.castle.Castle;
import com.efs.vampirehunters.castle.CastleGenerator;
import com.efs.vampirehunters.feature.GraveBlock;
import com.efs.vampirehunters.graveyard.Graveyard;
import com.efs.vampirehunters.graveyard.GraveyardGenerator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

@SuppressWarnings("deprecation")
public class VampireHunters implements ModInitializer {
	public static final EntityType<VampireEntity> VAMPIRE = Registry.register(Registry.ENTITY_TYPE,
			new Identifier("vampirehunters", "vampire"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, VampireEntity::new)
					.dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());

	public static final StructurePieceType GRAVEYARD_PIECE = GraveyardGenerator.Piece::new;
	private static final StructureFeature<DefaultFeatureConfig> GRAVEYARD_STRUCTURE = new Graveyard(
			DefaultFeatureConfig.CODEC);
	private static final ConfiguredStructureFeature<?, ?> GRAVEYARD_CONFIGURED = GRAVEYARD_STRUCTURE
			.configure(DefaultFeatureConfig.DEFAULT);

	public static final VampireBlock VAMPIRE_SPAWN = new VampireBlock(
			FabricBlockSettings.of(Material.METAL).strength(4.0f));
//	public static final GraveBlock GRAVESTONE = new GraveBlock(FabricBlockSettings.of(Material.STONE).strength(4.0f));

	public static final StructurePieceType CASTLE_PIECE = CastleGenerator.Piece::new;
	private static final StructureFeature<DefaultFeatureConfig> CASTLE_STRUCTURE = new Castle(
			DefaultFeatureConfig.CODEC);
	private static final ConfiguredStructureFeature<?, ?> CASTLE_CONFIGURED = CASTLE_STRUCTURE
			.configure(DefaultFeatureConfig.DEFAULT);

	@Override
	public void onInitialize() {
		Registry.register(Registry.STRUCTURE_PIECE, new Identifier("vampirehunters", "dracula_castle"), CASTLE_PIECE);
		FabricStructureBuilder.create(new Identifier("vampirehunters", "dracula_castle"), CASTLE_STRUCTURE)
				.step(GenerationStep.Feature.SURFACE_STRUCTURES).defaultConfig(100, 8, 10479212).adjustsSurface()
				.register();

		Registry.register(Registry.STRUCTURE_PIECE, new Identifier("vampirehunters", "graveyard"), GRAVEYARD_PIECE);
		FabricStructureBuilder.create(new Identifier("vampirehunters", "graveyard"), GRAVEYARD_STRUCTURE)
				.step(GenerationStep.Feature.SURFACE_STRUCTURES).defaultConfig(100, 8, 458735).adjustsSurface()
				.register();

		FabricDefaultAttributeRegistry.register(VAMPIRE, VampireEntity.createVampireAttributes());

		Registry.register(Registry.BLOCK, new Identifier("vampirehunters", "vampire_block"), VAMPIRE_SPAWN);
		Registry.register(Registry.ITEM, new Identifier("vampirehunters", "vampire_block"),
				new BlockItem(VAMPIRE_SPAWN, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));

	//	Registry.register(Registry.BLOCK, new Identifier("vampirehunters", "graves"), GRAVESTONE);
	//	Registry.register(Registry.ITEM, new Identifier("vampirehunters", "graves"),
	//			new BlockItem(GRAVESTONE, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));

		RegistryKey<ConfiguredStructureFeature<?, ?>> graveyardConfigured = RegistryKey
				.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier("vampirehunters", "graveyard"));
		BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
				new Identifier("vampirehunters", "graveyard"), GRAVEYARD_CONFIGURED);
		BiomeModifications.addStructure(BiomeSelectors.categories(Category.FOREST, Category.EXTREME_HILLS, Category.JUNGLE, Category.PLAINS, Category.SAVANNA, Category.TAIGA, Category.SWAMP), graveyardConfigured);

		RegistryKey<ConfiguredStructureFeature<?, ?>> castleConfigured = RegistryKey
				.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier("vampirehunters", "dracula_castle"));
		BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
				new Identifier("vampirehunters", "dracula_castle"), CASTLE_CONFIGURED);
		BiomeModifications.addStructure(BiomeSelectors.categories(Category.FOREST, Category.EXTREME_HILLS, Category.JUNGLE, Category.PLAINS, Category.SAVANNA, Category.TAIGA, Category.SWAMP), castleConfigured);
	}
}