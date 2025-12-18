package de.jaypi4c.mineduino.communication;

import org.schlunzis.jduino.channel.ChannelMessageListener;
import org.schlunzis.jduino.channel.serial.SerialDevice;
import org.schlunzis.jduino.channel.serial.SerialDeviceConfiguration;
import org.schlunzis.jduino.protocol.tlv.TLV;
import org.schlunzis.jduino.simple.SimpleChannel;

public class ChannelManager {

    public static final SimpleChannel simpleChannel = SimpleChannel.create();

    public static void init() {
        SerialDevice device = new SerialDevice("Super duper fast serial", "/dev/ttyACM0");
        SerialDeviceConfiguration serialConfig = new SerialDeviceConfiguration(device, 250000);
        simpleChannel.open(serialConfig);
    }

    public static void addListener(ChannelMessageListener<TLV> listener) {
        simpleChannel.addMessageListener(listener);
    }

}
