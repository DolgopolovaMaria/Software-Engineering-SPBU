package Server;

import java.net.Socket;
import java.util.List;
import java.net.ServerSocket;

public class Server {

    public static String serverIP = "127.0.0.1";

    private List<Filters.FilterType> filters;
    private ServerSocket serverSocket;

    private volatile boolean work;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        filters = Filters.getAvailableFilters();
        work = true;
    }

    public void start(){
        while (work) {
            newClient();
        }
    }

    public List<Filters.FilterType> getFilters(){
        return filters;
    }

    private void newClient(){
        try {
            Socket socket = serverSocket.accept();
            new Thread(new ServerSendReceive(socket, this)).start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
