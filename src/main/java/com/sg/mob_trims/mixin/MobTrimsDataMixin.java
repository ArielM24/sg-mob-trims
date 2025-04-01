package com.sg.mob_trims.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sg.mob_trims.IEntityDataSaver;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

@Mixin(Entity.class)
public abstract class MobTrimsDataMixin implements IEntityDataSaver{
    private NbtCompound persistentData;

    @Override
    public NbtCompound getSGMobTrimsPersistentData(){
        if(this.persistentData == null){
            this.persistentData = new NbtCompound();
        }
        return this.persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info){
        if(persistentData != null){
            nbt.put("sg_mob_trims_persistent_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if(nbt.contains("sg_mob_trims_persistent_data")){
            persistentData = nbt.getCompound("sg_mob_trims_persistent_data").get();
        }
    }
}
