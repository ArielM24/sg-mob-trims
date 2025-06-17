package com.sg.mob_trims;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SGMobTrims implements ModInitializer {
	public static final String MOD_ID = "sg-mob-trims";

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
			
			if(((IEntityDataSaver)entity).getChecked()){
				return;
			}
			((IEntityDataSaver)entity).setChecked(true);

			ItemStack head = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.HEAD);
			ItemStack chest = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.CHEST);
			ItemStack legs = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.LEGS);
			ItemStack feet = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.FEET);
			boolean hasArmorTrim = head.get(DataComponentTypes.TRIM) != null || chest.get(DataComponentTypes.TRIM) != null ||legs.get(DataComponentTypes.TRIM) != null ||feet.get(DataComponentTypes.TRIM) != null;
			if(hasArmorTrim){
				return;
			}
			applyTrim(entity, world);
		});	
	}

	public static void applyTrim(Entity entity, ServerWorld world){
		try{
			HostileEntity hostileEntity = null;
			if(entity instanceof ZombieEntity){
				hostileEntity = (ZombieEntity)entity;
			}else if(entity instanceof SkeletonEntity){
				hostileEntity = (SkeletonEntity)entity;
			}

			ItemStack head = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.HEAD);
			ItemStack chest = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.CHEST);
			ItemStack legs = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.LEGS);
			ItemStack feet = ((HostileEntity)entity).getEquippedStack(EquipmentSlot.FEET);
			
			Reference<ArmorTrimMaterial> trimMaterial = world.getRegistryManager().getOrThrow(RegistryKeys.TRIM_MATERIAL).getRandom(world.random).get();
			Reference<ArmorTrimPattern> trimPattern = world.getRegistryManager().getOrThrow(RegistryKeys.TRIM_PATTERN).getRandom(world.random).get();

			ArmorTrim at = new ArmorTrim(trimMaterial, trimPattern);
			head.set(DataComponentTypes.TRIM, at);
			hostileEntity.equipStack(EquipmentSlot.HEAD, head);
			chest.set(DataComponentTypes.TRIM, at);
			hostileEntity.equipStack(EquipmentSlot.CHEST, head);
			legs.set(DataComponentTypes.TRIM, at);
			hostileEntity.equipStack(EquipmentSlot.LEGS, head);
			feet.set(DataComponentTypes.TRIM, at);
			hostileEntity.equipStack(EquipmentSlot.FEET, head);
		}catch(Exception ex){
			LOGGER.info(ex.toString());
		}
	}

}