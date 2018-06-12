import org.usb4java.LibUsb;
import org.usb4java.DeviceHandle;
import java.nio.ByteBuffer;

class UsbTransfer {

	private static final byte USBASP_I2C_SLA_CHECK = 100;
	private static final byte USBASP_FUNC_I2C_BITRATE = 101;
	private static final byte USBASP_I2C_START = 120;
	private static final byte USBASP_I2C_STOP = 121;

	private static final int DEFAULT_TIMEOUT = 5000;
	
	public static int sendI2CSlaCheck(DeviceHandle handle, short i2cAddress) {
		ByteBuffer packet = ByteBuffer.allocateDirect(8);
		int transfered = LibUsb.controlTransfer(handle,
				(byte) 0xC0,
				(byte) USBASP_I2C_SLA_CHECK,
				(short) i2cAddress,
				(short) 0, packet, DEFAULT_TIMEOUT);
		//System.out.println("Response from I2C SLA check: " + packet.get());
		return transfered;
	}

	public static int sendI2CBitrate(DeviceHandle handle, short i2cAddress) {
		ByteBuffer packet = ByteBuffer.allocateDirect(8);
		int transfered = LibUsb.controlTransfer(handle,
				(byte) 0xC0,
				(byte) USBASP_FUNC_I2C_BITRATE,
				(short) i2cAddress,
				(short) 0, packet, DEFAULT_TIMEOUT);
		return transfered;
	}

	public static int sendI2CStart(DeviceHandle handle, short i2cAddress) {
		ByteBuffer packet = ByteBuffer.allocateDirect(8);
		int transfered = LibUsb.controlTransfer(handle,
				(byte) 0x40,
				(byte) USBASP_I2C_START,
				(short) 0,
				(short) 0, packet, DEFAULT_TIMEOUT);
		return transfered;
	}

	public static int sendI2CStop(DeviceHandle handle, short i2cAddress) {
		ByteBuffer packet = ByteBuffer.allocateDirect(8);
		int transfered = LibUsb.controlTransfer(handle,
				(byte) 0xC0,
				(byte) USBASP_I2C_STOP,
				(short) 0,
				(short) 0, packet, DEFAULT_TIMEOUT);
		return transfered;
	}
}
