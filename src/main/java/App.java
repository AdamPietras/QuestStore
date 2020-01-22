import com.sun.net.httpserver.HttpServer;
import handlers.ManagePupilsHandler;
import handlers.MentorHandler;
import handlers.StoreHandler;
import handlers.loginHandler;

import java.net.InetSocketAddress;


public class App {
    public static void main(String[] args) throws Exception {
        // create a server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // set routes

        server.createContext("/cyberStore", new loginHandler());
        server.createContext("/mentor/homepage", new MentorHandler());
        server.createContext("/mentor/store", new StoreHandler());
        server.createContext("/mentor/students", new ManagePupilsHandler());

        server.setExecutor(null); // creates a default executor

        // start listening
        server.start();
    }
}
