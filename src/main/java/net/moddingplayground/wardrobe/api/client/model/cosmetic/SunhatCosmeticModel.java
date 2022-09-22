package net.moddingplayground.wardrobe.api.client.model.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

import static net.minecraft.client.render.entity.model.EntityModelPartNames.*;
import static net.moddingplayground.wardrobe.api.client.model.WardrobeModelPartNames.*;

@Environment(EnvType.CLIENT)
public class SunhatCosmeticModel extends CosmeticModel {
    private final ModelPart hat, brim;

    public SunhatCosmeticModel(ModelPart root) {
        this.hat = root.getChild(HAT);
        this.brim = root.getChild(BRIM).setTinted();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        root.addChild(
            HAT,
            ModelPartBuilder.create()
                            .uv(0, 0)
                            .cuboid(-8.0F, -4.35F, -8.0F, 16.0F, 0.0F, 16.0F)
                            .uv(0, 16)
                            .cuboid(-4.0F, -9.0F, -4.0F, 8.0F, 4.0F, 8.0F, new Dilation(0.65F)),
            ModelTransform.NONE
        );

        root.addChild(
            BRIM,
            ModelPartBuilder.create()
                            .uv(0, 28)
                            .cuboid(-4.0F, -9.0F, -4.0F, 8.0F, 4.0F, 8.0F, new Dilation(0.65F)),
            ModelTransform.NONE
        );

        return TexturedModelData.of(data, 64, 64);
    }

    @Override
    public void setAngles(AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, PlayerEntityModel<PlayerEntity> contextModel) {
        this.hat.copyTransform(contextModel.hat);
        this.brim.copyTransform(contextModel.hat);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return List.of(this.hat, this.brim);
    }
}
