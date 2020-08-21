import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Main {
    public static void main(String[] args){
        try {
            final DatagramSocket socket = new DatagramSocket();

            final double[] offsets = new double[10];
            for (int i = 0; i < 10; i += 1) {
                final byte[] requestData = new byte[48];
                requestData[0] = (byte) 27;
                final InetAddress address = InetAddress.getByName("ntp2.sth.netnod.se");
                final DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, 123);
                final double sourceTimeStamp = (System.currentTimeMillis() / 1000.0) + 2208988800.0;
                writeTime(requestPacket.getData(), 40, sourceTimeStamp);
                socket.send(requestPacket);

                final DatagramPacket responsePacket = new DatagramPacket(requestData, requestData.length);
                socket.setSoTimeout(10000);
                socket.receive(responsePacket);
                final double destinationTimestamp = (System.currentTimeMillis() / 1000.0) + 2208988800.0;

                final byte[] responseData = responsePacket.getData();
                final double originateTimestamp = decodeTimestamp(responseData, 24);
                final double receiveTimestamp = decodeTimestamp(responseData, 32);
                final double transmitTimestamp = decodeTimestamp(responseData, 40);

                offsets[i] = ((receiveTimestamp - originateTimestamp) + (transmitTimestamp - destinationTimestamp)) / 2;
            }

            final double localClockOffset = Arrays.stream(offsets).average().getAsDouble();
            socket.close();
            long millisecondsSince1970 = (long) (System.currentTimeMillis() + localClockOffset * 1000);
            final Date date = new Date(millisecondsSince1970);
            final String time = new SimpleDateFormat("HH:mm:ss").format(date);
            System.out.println(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static short unsignedByteToShort(byte b) {
        if ((b & 0x80) == 0) return b;
        else return (short) (0b10000000 | (b & 0b1111111));
    }

    public static double decodeTimestamp(byte[] array, int pointer) {
        double r = 0.0;
        for (int i = 0; i < 8; i++) {
            r += unsignedByteToShort(array[pointer + i]) * Math.pow(2, (3 - i) * 8);
        }

        return r;
    }

    public static void writeTime(byte[] array, int offset, double time) {
        for (int i = 0; i < 8; i++) {
            double base = Math.pow(2, (3 - i) * 8);
            array[offset + i] = (byte) (time / base);
            time = time - (unsignedByteToShort(array[offset + i]) * base);
        }
    }
}