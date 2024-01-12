package com.susen36.terraworld.init;


import com.mojang.datafixers.util.Pair;
import com.susen36.terraworld.TerraWorld;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemsInit {
    //https://www.mr-pineapple.co.uk/tutorials/items
	private static final ItemsInit reflector = new ItemsInit();
	private static Pair<Model, List<ResourceLocation>> storedModel = Pair.of(Model.Simple, new ArrayList<>());
	@Deprecated // will be cleared after register.
	public static List<Pair<RegistryObject<Item>, Pair<Model, List<ResourceLocation>>>> modelList = new ArrayList<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraWorld.MODID);
	public static final Collection<RegistryObject<Item>> SPAWN_EGGS = new ArrayList<>();
	public static final RegistryObject<Item> SCULK_ENDERMAN_CLEAVER = ITEMS.register("sculk_enderman_cleaver", () -> new Item(new Item.Properties()){
		@Override
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable("tooltip.sculkhorde.sculk_enderman_cleaver"));
		}
	});

    public static final RegistryObject<Item> SCULK_MATTER = ITEMS.register("sculk_matter", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> CRYING_SOULS = ITEMS.register("crying_souls", () -> new Item(new Item.Properties()){
		@Override
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable("tooltip.sculkhorde.crying_souls"));
		}
	});

	public static final RegistryObject<Item> PURE_SOULS = ITEMS.register("pure_souls", () -> new Item(new Item.Properties()){
		@Override
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable("tooltip.sculkhorde.pure_souls"));
		}
	});

	public static final RegistryObject<Item> ESSENCE_OF_PURITY = ITEMS.register("essence_of_purity", () -> new Item(new Item.Properties()){
		@Override
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable("tooltip.sculkhorde.essence_of_purity"));
		}
	});


	public static final RegistryObject<Item> CALCITE_CLUMP = ITEMS.register("calcite_clump",
			() -> new Item(new Item.Properties()){
				@Override
				public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
					tooltip.add(Component.translatable("tooltip.sculkhorde.calcite_clump"));
				}
			});

	public static final RegistryObject<Item> CHUNK_O_BRAIN = ITEMS.register("chunk_o_brain", () -> new Item(new Item.Properties()){
		@Override
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable("tooltip.sculkhorde.chunk_o_brain"));
		}
	});

	public static final RegistryObject<Item> DORMANT_HEART_OF_THE_HORDE = ITEMS.register("dormant_heart_of_the_horde", () -> new Item(new Item.Properties()){
		@Override
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable("tooltip.sculkhorde.dormant_heart_of_the_horde"));
		}
	});

	public static final RegistryObject<Item> HEART_OF_THE_HORDE = ITEMS.register("heart_of_the_horde", () -> new Item(new Item.Properties()){
		@Override
		public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
			tooltip.add(Component.translatable("tooltip.sculkhorde.heart_of_the_horde"));
		}
	});
	public static RegistryObject<Item> item(String name, Supplier<Item> sup){
		RegistryObject<Item> itemObj = ITEMS.register(name, sup);
		if (storedModel.getFirst() != Model.Modeled){
			modelList.add(Pair.of(itemObj, storedModel));
		}
		storedModel = Pair.of(Model.Simple, new ArrayList<>());
		return itemObj;
	}

	protected static ItemsInit model(Model model, ResourceLocation... res){
		storedModel = Pair.of(model, List.of(res));
		return reflector;
	}

	protected static ItemsInit model(Model model, List<ResourceLocation> res){
		storedModel = Pair.of(model, res);
		return reflector;
	}

	public enum Model {
		Simple, Block, SpawnEgg, SeedPacket, Modeled
	}
}
