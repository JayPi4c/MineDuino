package de.jaypi4c.mineduino.mixin;

import de.jaypi4c.mineduino.gui.screens.SettingsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int x = this.width / 2 - buttonWidth / 2; // center horizontally
        int y = this.height - 3 * buttonHeight; // adjust vertical position as needed


        this.addDrawableChild(
                ButtonWidget.builder(Text.of("Mineduino"),
                                _ -> this.client.setScreen(new SettingsScreen(this, Text.of("MineDuino Settings")))
                        ).dimensions(x, y, buttonWidth, buttonHeight)
                        .build()
        );
    }
}