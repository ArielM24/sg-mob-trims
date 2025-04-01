package com.sg.mob_trims;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.world.Difficulty;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SGMobTrims implements ModInitializer {
	public static final String MOD_ID = "sg-mob-trims";

	public static final String[] trimPatterns = new String[]{"sentry", "dune", "coast", "wild", "tide", "ward", "vex", "rib", "snout", "eye", "spire", "wayfinder", "raiser", "shaper", "host", "silence"};
	public static final String[] trimMaterials = new String[]{"iron", "copper", "gold", "lapis", "emerald", "diamond", "netherite", "redstone", "amethyst", "quartz"};
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, world)->{
			if(!entity.getWorld().getDifficulty().equals(Difficulty.HARD)){
				return;
			}
			boolean isZombie = (entity instanceof ZombieEntity);
			boolean isSkeleton = (entity instanceof SkeletonEntity);
			if(!isZombie && !isSkeleton){
				return;
			}
			NbtElement check = ((IEntityDataSaver)entity).getSGMobTrimsPersistentData().get("sg_mob_trims");
			if(check != null){
				return;
			}
			((IEntityDataSaver)entity).getSGMobTrimsPersistentData().putString("sg_mob_trims", "checked");
			Random r = new Random();
			if(r.nextBoolean()){
				return;
			}
			applyTrim(entity);
		});	
	}

	public static void applyTrim(Entity zombie){
		try{
			Random r = new Random();
			String pattern = trimPatterns[r.nextInt(trimPatterns.length)];
			String material = trimMaterials[r.nextInt(trimMaterials.length)];
			String trim = "components:{trim:{material:"+material+",pattern:"+pattern+"}}";
			String equipment = "";

			NbtCompound n1 = new NbtCompound();
			zombie.writeNbt(n1);
			if(!n1.contains("equipment")){
				return;
			}
			equipment = n1.get("equipment").toString();
			LOGGER.info(equipment);
			if(equipment.contains("trim")){
				return;
			}
			equipment = equipment.replaceAll("_helmet\"", "_helmet\"," + trim);
         	equipment = equipment.replaceAll("_boots\"", "_boots\"," + trim);
         	equipment = equipment.replaceAll("_chestplate\"", "_chestplate\"," + trim);
         	equipment = equipment.replaceAll("_leggings\"", "_leggings\"," + trim);
			LOGGER.info(equipment);
			if(equipment.isEmpty()){
				return;
			}
			NbtCompound n2 = StringNbtReader.readCompound(equipment);
			n1.put("equipment", n2);
			zombie.readNbt(n1);
		}catch(Exception ex){
			LOGGER.info(ex.toString());
		}
	}
}