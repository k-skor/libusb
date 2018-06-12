#!/bin/bash
javac -cp ".:usb4java-1.2.0/lib/usb4java-1.2.0.jar" UsbCom.java && \
sudo java -cp ".:usb4java-1.2.0/lib/usb4java-1.2.0.jar:usb4java-1.2.0/lib/libusb4java-1.2.0-linux-x86_64" UsbCom 16c0 05dc
