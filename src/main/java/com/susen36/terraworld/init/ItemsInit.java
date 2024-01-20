package com.susen36.terraworld.init;


import com.mojang.datafixers.util.Pair;
import com.susen36.terraworld.TerraWorld;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemsInit {
    //https://www.mr-pineapple.co.uk/tutorials/items
	private static final ItemsInit reflector = new ItemsInit();
	private static Pair<Model, List<ResourceLocation>> storedModel = Pair.of(Model.Simple, new ArrayList<>());
	@Deprecated // will be cleared after register.
	public static List<Pair<RegistryObject<Item>, Pair<Model, List<ResourceLocation>>>> modelList = new ArrayList<>();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraWorld.MODID);

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
