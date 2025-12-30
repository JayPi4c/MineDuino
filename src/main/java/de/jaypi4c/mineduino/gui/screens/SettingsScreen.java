package de.jaypi4c.mineduino.gui.screens;

import de.jaypi4c.mineduino.MineDuino;
import de.jaypi4c.mineduino.communication.ChannelManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.schlunzis.jduino.channel.serial.SerialDevice;
import org.schlunzis.jduino.channel.serial.SerialDeviceConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SettingsScreen extends Screen {

    private static final Text TITLE_TEXT = Text.translatable("options.mineduino.title");
    private final Screen parent;
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this, 61, 33);
    private int baudRate = 9600;
    private int LEDPin = 13;
    private SerialDevice serialDevice = null;


    public SettingsScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        DirectionalLayoutWidget directionalLayoutWidget = this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(8));
        directionalLayoutWidget.add(new TextWidget(TITLE_TEXT, this.textRenderer), Positioner::alignHorizontalCenter);

        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(4).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);

        adder.add(ButtonWidget.builder(
                Text.of("Hello World"),
                _ -> this.client.getToastManager()
                        .add(
                                SystemToast.create(
                                        this.client,
                                        SystemToast.Type.NARRATOR_TOGGLE,
                                        Text.of("Hello World!"),
                                        Text.of("This is a toast."))
                        )).build());

        List<SerialDevice> devices = new ArrayList<>(ChannelManager.getInstance().getChannel().getDevices());
        CyclingButtonWidget<SerialDevice> cyclingButton = CyclingButtonWidget.<SerialDevice>builder(
                        (device) -> Text.of(device == null ? "None" : device.getDisplayName()))
                .initially(devices.getFirst())
                .values(devices)
                .build(Text.of("Port"), (_, device) -> {
                    MineDuino.LOGGER.debug("Selected serial device changed to {}", device.getDisplayName());
                    serialDevice = device;
                });

        adder.add(cyclingButton);

        CyclingButtonWidget<Integer> baudRateButton = CyclingButtonWidget.<Integer>builder(
                        (rate) -> Text.of(rate.toString()))
                .initially(9600)
                .values(9600, 19200, 38400, 57600, 115200)
                .build(Text.of("Baud Rate"), (_, rate) -> {
                    MineDuino.LOGGER.debug("Baud rate changed to {}", rate);
                    baudRate = rate;
                });
        adder.add(baudRateButton);

        CyclingButtonWidget<Integer> ledPinButton = CyclingButtonWidget.<Integer>builder(
                        (pin) -> Text.of(pin.toString()))
                .initially(13)
                .values(13, 25).build(Text.of("LED Pin"), (_, pin) -> {
                    MineDuino.LOGGER.debug("LED pin changed to {}", pin);
                    LEDPin = pin;
                });
        adder.add(ledPinButton);

        this.layout.addBody(gridWidget);
        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, getDoneAction()).width(200).build());
        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    private ButtonWidget.PressAction getDoneAction() {
        return _ -> {
            if (serialDevice != null) {
                SerialDeviceConfiguration configuration = new SerialDeviceConfiguration(serialDevice, baudRate);
                ChannelManager.getInstance().configureChannel(configuration);
                ChannelManager.getInstance().setLEDPin(LEDPin);
                MineDuino.LOGGER.info("Settings applied: Device={}, BaudRate={}, LEDPin={}", serialDevice.getDisplayName(), baudRate, LEDPin);
            } else {
                MineDuino.LOGGER.warn("No serial device selected, settings not applied.");
            }
            this.close();
        };
    }

    @Override
    protected void initTabNavigation() {
        this.layout.refreshPositions();
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}