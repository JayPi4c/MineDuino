package de.jaypi4c.mineduino.communication;

import org.schlunzis.jduino.channel.Channel;
import org.schlunzis.jduino.channel.serial.SerialDevice;
import org.schlunzis.jduino.channel.serial.SerialDeviceConfiguration;
import org.schlunzis.jduino.simple.SimpleChannel;

public class ChannelManager {

    public static final SimpleChannel simpleChannel = Channel.builder().simple().build();

    public static void init() {
        SerialDevice device = new SerialDevice("Super duper fast serial", "/dev/ttyACM0");
        SerialDeviceConfiguration serialConfig = new SerialDeviceConfiguration(device, 250000);
        simpleChannel.open(serialConfig);
    }


}
