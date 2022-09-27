package net.moddingplayground.wardrobe.api.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.moddingplayground.wardrobe.api.Wardrobe;
import net.moddingplayground.wardrobe.api.client.network.WardrobeClientNetworking;
import net.moddingplayground.wardrobe.api.cosmetic.data.CosmeticSlot;

import java.util.Calendar;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WardrobeScreen extends Screen {
    public static final Identifier BACKGROUND_TEXTURE = new Identifier(Wardrobe.MOD_ID, "textures/gui/cosmetics.png");
    public static final Identifier SUPPORTED_STATUSES_TEXTURE = new Identifier(Wardrobe.MOD_ID, "textures/gui/supported_statuses.png");

    public static final List<Text> UNSUPPORTED_TOOLTIP_TEXT = List.of(
        Text.translatable("ui.%s.cosmetics.unsupported_server.line0".formatted(Wardrobe.MOD_ID)),
        Text.translatable("ui.%s.cosmetics.unsupported_server.line1".formatted(Wardrobe.MOD_ID))
    );

    public static final int BOX_WIDTH = 96;
    public static final int BOX_HEIGHT = 96;

    private int x, y;
    private int supportX, supportY;
    private int titleX, titleY;
    private boolean frog;

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

        int widgetX = (BOX_WIDTH / 2) - (10 / 2) - 3;
        int widgetY = 20;
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            this.addSlotWidget(widgetX, widgetY + (slot.ordinal() * 16), slot);
        }

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) + 1 == 3 && calendar.get(Calendar.DATE) == 20) {
            this.frog = true;
        }
    }

    public void addSlotWidget(int x, int y, CosmeticSlot slot) {
        SlotWidget widget = new SlotWidget(this.x + x, this.y + y, slot);
        this.addDrawable(widget);
        this.addSelectableChild(widget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        /* Background */

        this.renderBackground(matrices);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        this.drawTexture(matrices, this.x, this.y, 0, 0, BOX_WIDTH, BOX_HEIGHT);

        /* Title */

        this.textRenderer.draw(matrices, this.title, this.titleX, this.titleY, 0x003F3F3F);

        /* Render Drawables, Etc. */

        super.render(matrices, mouseX, mouseY, delta);

        /* Status */

        if (WardrobeClientNetworking.isUnsupported(this.client)) {
            RenderSystem.setShaderTexture(0, SUPPORTED_STATUSES_TEXTURE);
            this.drawSupportTexture(matrices, 0, 0, WardrobeClientNetworking.isUnsupported(this.client) ? 0 : 7, 11, 10, 7);

            if (this.isMouseWithinBounds(mouseX, mouseY, this.supportX, this.supportY, 10, 7)) {
                this.renderTooltip(matrices, UNSUPPORTED_TOOLTIP_TEXT, mouseX, mouseY);
            }
        }
    }

    public boolean isMouseWithinBounds(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void drawSupportTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        drawTexture(matrices, this.supportX + x, this.supportY + y, u, v, width, height, 32, 32);
    }

    @Environment(EnvType.CLIENT)
    public class SlotWidget extends PressableWidget {
        private final CosmeticSlot slot;
        private final WardrobeScreen parent;

        public SlotWidget(int x, int y, CosmeticSlot slot) {
            super(x, y, 16, 16, slot.getDisplayText());
            this.slot = slot;
            this.parent = WardrobeScreen.this;
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            boolean hovered = this.isHovered();

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);

            int v = this.getYImage(hovered);
            this.drawTexture(matrices, this.x, this.y, 96, (v - 1) * this.width, this.width, this.height);

            int su = 112 + (v - 1) * this.width;
            int sv = (this.slot.ordinal() * this.width) + (this.parent.frog ? 64 : 0);
            this.drawTexture(matrices, this.x, this.y, su, sv, this.width, this.height);

            if (hovered) {
                this.renderTooltip(matrices, mouseX, mouseY);
            }
        }

        @Override
        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        }

        @Override
        public void onPress() {
            MinecraftClient client = this.parent.client;
            client.setScreen(new WardrobeSlotScreen(this.parent, this.slot));
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }
}
