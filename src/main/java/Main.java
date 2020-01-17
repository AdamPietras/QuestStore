import com.sun.net.httpserver.HttpServer;
import httpHandlers.StudentHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {


    static void runServer(){
        try{
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
            //set routes
            httpServer.createContext("/students", new StudentHandler());
            httpServer.setExecutor(null);
            httpServer.start();
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        runServer();
    }
}