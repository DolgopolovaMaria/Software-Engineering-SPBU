package SenderReciever;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SenderReciever {
    public static void send(DataOutputStream out, String message) {
        try {
            out.flush();
            out.writeUTF(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String receive(DataInputStream in) {
        String str = "";
        try {
            str = in.readUTF();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void received(DataOutputStream out) {
        try {
            out.flush();
            out.writeUTF("received");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



}
