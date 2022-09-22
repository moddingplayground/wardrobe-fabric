package net.moddingplayground.wardrobe.api.client.render.cosmetic.manager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.resource.ResourceManager;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.CosmeticRenderer;

@Environment(EnvType.CLIENT)
public interface CosmeticRendererFactory {
    CosmeticRenderer create(Context context);

    @Environment(EnvType.CLIENT)
    record Context(
        EntityRenderDispatcher renderDispatcher, ItemRenderer itemRenderer,
        BlockRenderManager blockRenderManager, HeldItemRenderer heldItemRenderer,
        ResourceManager resourceManager, EntityModelLoader modelLoader,
        TextRenderer textRenderer
    ) {
        public ModelPart getPart(EntityModelLayer layer) {
            return this.modelLoader.getModelPart(layer);
        }
    }
}
