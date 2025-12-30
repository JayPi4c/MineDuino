package de.jaypi4c.mineduino.gui.screens;

import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.communication.ChannelManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.schlunzis.jduino.channel.serial.SerialDevice;
import org.schlunzis.jduino.channel.serial.SerialDeviceConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SettingsScreen extends Screen {
    public SettingsScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Hello World"), _ -> {
            // When the button is clicked, we can display a toast to the screen.
            this.client.getToastManager().add(
                    SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
            );
        }).dimensions(40, 40, 120, 20).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        List<SerialDevice> devices = new ArrayList<>(ChannelManager.simpleChannel.getDevices());


        CyclingButtonWidget<SerialDevice> cyclingButton = CyclingButtonWidget.<SerialDevice>builder(
                        (device) -> Text.of(device == null ? "None" : device.getDisplayName()))
                .initially(devices.getFirst())
                .values(devices)
                .build(40, 70, 120, 20, Text.of("Port"), (_, device) -> {
                    MineDuino.LOGGER.info("Cycling button");
                    if (device != null) {
                        ChannelManager.simpleChannel.close();
                        ChannelManager.simpleChannel.open(new SerialDeviceConfiguration(device, 9600));
                    }
                });

        // Register the button widget.
        this.addDrawableChild(buttonWidget);
        this.addDrawableChild(cyclingButton);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
        context.drawText(this.textRenderer, "Special Button", 40, 40 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }
}