package com.sg.mob_trims.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sg.mob_trims.IEntityDataSaver;

import net.minecraft.entity.Entity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

@Mixin(Entity.class)
public abstract class MobTrimsDataMixin implements IEntityDataSaver{
    private Boolean checked = false;

    @Override
    public Boolean getChecked(){
        return checked;
    }

    @Override
    public void setChecked(Boolean c){
        checked = c;
    }


    @Inject(method = "writeData", at = @At("HEAD"))
    protected void injectWriteMethod(WriteView data, CallbackInfo info){
       data.putBoolean("sg_mob_trims_persistent_data", checked);
    }

    @Inject(method = "readData", at = @At("HEAD"))
    protected void injectReadMethod(ReadView data, CallbackInfo info){
        checked = data.getBoolean("sg_mob_trims_persistent_data", false);
    }
}
