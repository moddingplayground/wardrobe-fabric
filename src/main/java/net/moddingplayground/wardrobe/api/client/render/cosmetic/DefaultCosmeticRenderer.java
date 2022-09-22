package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.moddingplayground.wardrobe.api.client.model.cosmetic.CosmeticModel;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticRendererFactory;
import net.moddingplayground.wardrobe.api.cosmetic.CosmeticInstance;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class DefaultCosmeticRenderer implements CosmeticRenderer {
    private final CosmeticModel model;

    public DefaultCosmeticRenderer(CosmeticRendererFactory.Context context, EntityModelLayer layer, Function<ModelPart, CosmeticModel> model) {
        this.model = model.apply(context.getPart(layer));
    }

    @Override
    public void setAngles(AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, PlayerEntityModel<PlayerEntity> contextModel) {
        this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, contextModel);
    }

    @Override
    public void render(CosmeticRendererContext context, CosmeticInstance cosmetic) {
        MatrixStack matrices = context.matrices();
        VertexConsumerProvider vertices = context.vertices();
        RenderLayer layer = this.model.getLayer(this.getTexture());
        int color = cosmetic.color();
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >>  8) & 0xFF) / 255f;
        float b =  (color        & 0xFF) / 255f;
        this.model.render(matrices, vertices.getBuffer(layer), context.light(), OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
    }
}
