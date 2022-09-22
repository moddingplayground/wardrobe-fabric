package net.moddingplayground.wardrobe.api.client.render.cosmetic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.model.WardrobeEntityModelLayers;
import net.moddingplayground.wardrobe.api.client.model.cosmetic.SunhatCosmeticModel;
import net.moddingplayground.wardrobe.api.client.render.cosmetic.manager.CosmeticRendererFactory;

@Environment(EnvType.CLIENT)
public class SunhatCosmeticRenderer extends DefaultCosmeticRenderer {
    public SunhatCosmeticRenderer(CosmeticRendererFactory.Context context) {
        super(context, WardrobeEntityModelLayers.SUNHAT, SunhatCosmeticModel::new);
    }

    @Override
    public Identifier getTexture() {
        return new Identifier(Wardrobe.MOD_ID, "textures/cosmetic/sunhat.png");
    }
}
