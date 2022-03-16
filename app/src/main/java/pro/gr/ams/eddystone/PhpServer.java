package pro.gr.ams.eddystone;

import java.net.InetAddress;

public class PhpServer {
    private String server;
    private boolean development;

    public PhpServer(){
    }

    public PhpServer(String Server) {
        this.server = Server;
    }

    public String getServer() {
        development = false;

        if (development == true) {
            server = "http://192.168.1.201:8888/eddystoneph.online/";
        }
        else {
            server = "https://eddystoneph.online/";
        }
        return server;
    }

    public boolean isServerReachable(String server)
    {
        try {
            InetAddress.getByName(server).isReachable(3000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
