package net.moddingplayground.wardrobe.api.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;

@Environment(EnvType.CLIENT)
public class WardrobeSlotScreen extends Screen {
    private final Screen parent;
    private final CosmeticSlot slot;

    protected WardrobeSlotScreen(Screen parent, CosmeticSlot slot) {
        super(slot.getDisplayText());
        this.parent = parent;
        this.slot = slot;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public CosmeticSlot getSlot() {
        return this.slot;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        /* Background */

        this.renderBackground(matrices);

        /* Render Drawables, Etc. */

        super.render(matrices, mouseX, mouseY, delta);
    }
}
