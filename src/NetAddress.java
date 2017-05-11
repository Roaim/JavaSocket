
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;

public class NetAddress {
	public static void main(String... arg) {
		try {
			InetAddress i = InetAddress.getLocalHost();
			System.out.println(i.getHostAddress());
			System.out.println(i.getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			System.out.println(getLocalIpAddress());
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	public static String getLocalIpAddress() throws SocketException {
		
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress()) {
					// String ip = Formatter.formatIpAddress(inetAddress.hashCode());

					return inetAddress.getHostAddress();
				}
			}
		}
		return null;
	}
}
