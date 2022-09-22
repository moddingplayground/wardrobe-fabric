package net.moddingplayground.wardrobe.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.moddingplayground.wardrobe.api.client.model.WardrobeModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(ModelPart.class)
public class ModelPartMixin implements WardrobeModelPart {
    private boolean tinted;

    @Unique
    @Override
    public ModelPart setTinted() {
        this.tinted = true;
        return (ModelPart) (Object) this;
    }

    @Unique
    @Override
    public boolean isTinted() {
        return this.tinted;
    }
}
