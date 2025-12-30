package de.jaypi4c.mineduino.communication;

import org.schlunzis.jduino.channel.ChannelMessageListener;
import org.schlunzis.jduino.channel.serial.SerialDeviceConfiguration;
import org.schlunzis.jduino.protocol.tlv.TLV;
import org.schlunzis.jduino.simple.SimpleChannel;

public class ChannelManager {

    public static final SimpleChannel simpleChannel = SimpleChannel.create();

    public static void init() {
        SerialDeviceConfiguration serialConfig = new SerialDeviceConfiguration(simpleChannel.getDevices().getFirst(), 250000);
        simpleChannel.open(serialConfig);
    }

    public static void addListener(ChannelMessageListener<TLV> listener) {
        simpleChannel.addMessageListener(listener);
    }

}
