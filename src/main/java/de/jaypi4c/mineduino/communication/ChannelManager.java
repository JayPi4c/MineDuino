package de.jaypi4c.mineduino.communication;

import de.jaypi4c.mineduino.MineDuino;
import org.schlunzis.jduino.channel.ChannelMessageListener;
import org.schlunzis.jduino.channel.serial.SerialDevice;
import org.schlunzis.jduino.channel.serial.SerialDeviceConfiguration;
import org.schlunzis.jduino.protocol.tlv.TLV;
import org.schlunzis.jduino.simple.SimpleChannel;

public class ChannelManager {

    private static final ChannelManager instance = new ChannelManager();
    private final SimpleChannel simpleChannel = SimpleChannel.create();
    private int LEDPin = 13;

    private ChannelManager() {
        addListener(message -> MineDuino.LOGGER.debug("Received message from serial device: {} | {}", message.getMessageType(), new String(message.getPayload())));
        SerialDevice defaultDevice = simpleChannel.getDevices().getFirst();
        SerialDeviceConfiguration configuration = new SerialDeviceConfiguration(defaultDevice, 9600);
        simpleChannel.open(configuration);
    }

    public static ChannelManager getInstance() {
        return instance;
    }

    public SimpleChannel getChannel() {
        return simpleChannel;
    }

    public int getLEDPin() {
        return LEDPin;
    }

    public void setLEDPin(int pin) {
        this.LEDPin = pin;
    }

    public void sendLEDCommand(boolean state) {
        simpleChannel.sendLEDCommand(LEDPin, state);
    }

    public void configureChannel(SerialDeviceConfiguration configuration) {
        simpleChannel.close();
        simpleChannel.open(configuration);
    }

    public void addListener(ChannelMessageListener<TLV> listener) {
        simpleChannel.addMessageListener(listener);
    }

}
