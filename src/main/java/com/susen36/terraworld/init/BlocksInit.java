package com.susen36.terraworld.init;


import com.susen36.terraworld.TerraWorld;
import com.susen36.terraworld.block.NetherDoorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlocksInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TerraWorld.MODID);

	//Method to Register Blocks & Register them as items
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
	{
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}

	//helper method to register a given block as a holdable item
	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block)
	{
		ItemsInit.ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties()));
	}

	public static final RegistryObject<Block> NETHER_DOOR =
			registerBlock("nether_door", () -> new NetherDoorBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.COLOR_BLACK)
					.requiresCorrectToolForDrops()
					.strength(30.0F, 1000.0F)
					.sound(SoundType.STONE)));


}
