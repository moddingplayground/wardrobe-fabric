package net.moddingplayground.wardrobe.api.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.network.WardrobeClientNetworking;

@Environment(EnvType.CLIENT)
public class WardrobeScreen extends Screen {
    public static final Identifier BACKGROUND_TEXTURE = new Identifier(Wardrobe.MOD_ID, "textures/gui/cosmetics.png");
    public static final Identifier SUPPORTED_STATUSES_TEXTURE = new Identifier(Wardrobe.MOD_ID, "textures/gui/supported_statuses.png");

    public static final int BOX_WIDTH = 96;
    public static final int BOX_HEIGHT = 96;

    private int x, y;
    private int supportX, supportY;
    private int titleX, titleY;
    private long openedAt;

    public WardrobeScreen() {
        super(Text.literal(Wardrobe.MOD_NAME));
    }

    @Override
    protected void init() {
        this.x = (this.width / 2) - (BOX_WIDTH / 2);
        this.y = (this.height / 2) - (BOX_HEIGHT / 2);
        this.supportX = this.x + BOX_WIDTH - 10 - 10;
        this.supportY = this.y + BOX_HEIGHT - 7 - 10;
        this.titleX = (int) ((this.width / 2f) - (this.textRenderer.getWidth(this.title) / 2f));
        this.titleY = (int) ((this.height / 2f) - (BOX_HEIGHT / 2f) + 6);
        this.openedAt = Util.getMeasuringTimeMs();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        /* Background */

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        this.drawTexture(matrices, this.x, this.y, 0, 0, BOX_WIDTH, BOX_HEIGHT);

        /* Status */

        if (WardrobeClientNetworking.isUnsupported(this.client)) {
            long ms = Util.getMeasuringTimeMs();
            if (ms - this.openedAt <= 3 * 1000L) {
                RenderSystem.setShaderTexture(0, SUPPORTED_STATUSES_TEXTURE);
                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (ms / 500L) % 2 == 0 ? 1.0F : 0.5F);
                this.drawSupportTexture(matrices, 0, 0, WardrobeClientNetworking.isUnsupported(this.client) ? 0 : 7, 11, 10, 7);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        /* Title */

        this.textRenderer.draw(matrices, this.title, this.titleX, this.titleY, 0x003F3F3F);

        super.render(matrices, mouseX, mouseY, delta);
    }

    public void drawSupportTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        drawTexture(matrices, this.supportX + x, this.supportY + y, u, v, width, height, 32, 32);
    }
}
