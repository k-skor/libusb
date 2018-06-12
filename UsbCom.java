import org.usb4java.LibUsb;
import org.usb4java.Device;
import org.usb4java.DeviceList;
import org.usb4java.LibUsbException;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import java.nio.ByteBuffer;

public class UsbCom {

	private static final short DEFAULT_I2C_ADDRESS = 0x00;

	public static Device findDevice(short vendorId, short productId) throws LibUsbException {
		DeviceList list = new DeviceList();
		int result = LibUsb.getDeviceList(null, list);
		if (result < 0) throw new LibUsbException("Unable to get device list", result);

		try {
			for (Device device: list) {
				DeviceDescriptor descriptor = new DeviceDescriptor();
				result = LibUsb.getDeviceDescriptor(device, descriptor);
				if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
				if (descriptor.idVendor() == vendorId && descriptor.idProduct() == productId) return device;
			}
		}
		finally {
			LibUsb.freeDeviceList(list, true);
		}

		return null;
	}

	public static void main(String[] args) {
		int result = LibUsb.init(null);
		System.out.format("USB initialized with result: %d\n", result);

		short[] vpId = new short[] { 0x16C0, 0x05DC };
		if (args.length >= 2) {
			try {
				short vId = Short.parseShort(args[0], 16);
				short pId = Short.parseShort(args[1], 16);
				vpId[0] = vId;
				vpId[1] = pId;
			} catch (NumberFormatException e) {}
		}
		try {
			System.out.println("Trying to find device with vendor ID: " + vpId[0] + " and product ID: " + vpId[1]);
			Device usbAsp = findDevice(vpId[0], vpId[1]);
			System.out.println("Device found: " + (usbAsp != null));

			DeviceHandle handle = new DeviceHandle();
			result = LibUsb.open(usbAsp, handle);
			if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to open USB device", result);
			try {
				result = LibUsb.claimInterface(handle, 0);
				if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to claim interface", result);
				try {
					UsbTransfer.sendI2CSlaCheck(handle, DEFAULT_I2C_ADDRESS);
					UsbTransfer.sendI2CBitrate(handle, DEFAULT_I2C_ADDRESS);
					UsbTransfer.sendI2CStart(handle, DEFAULT_I2C_ADDRESS);
					UsbTransfer.sendI2CStop(handle, DEFAULT_I2C_ADDRESS);
				}
				finally {
					result = LibUsb.releaseInterface(handle, 0);
					if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to release interface", result);
				}
			}
			finally {
				LibUsb.close(handle);
			}
		} catch (LibUsbException e) {
			System.out.println(e.toString());
		}
	}
}
