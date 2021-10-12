package com.efs.vampirehunters;

import com.efs.vampirehunters.bossmob.VampireBlock;
import com.efs.vampirehunters.bossmob.VampireEntity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class VampireHunters implements ModInitializer {
    public static final EntityType<VampireEntity> VAMPIRE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("vampirehunters", "vampire"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, VampireEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    
    public static final VampireBlock VAMPIRE_SPAWN = new VampireBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
	
	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(VAMPIRE, VampireEntity.createVampireAttributes());
		 Registry.register(Registry.BLOCK, new Identifier("vampirehunters", "vampire_block"), VAMPIRE_SPAWN);
		 Registry.register(Registry.ITEM, new Identifier("vampirehunters", "vampire_block"), new BlockItem(VAMPIRE_SPAWN, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
	}
}
