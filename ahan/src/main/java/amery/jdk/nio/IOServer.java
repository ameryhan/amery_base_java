package amery.jdk.nio;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ahan on 11/07/2017.
 */
public class IOServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOServer.class);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(2345));
        } catch (IOException ex) {
            LOGGER.error("Listen failed", ex);
            return;
        }
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream inputstream = socket.getInputStream();
                LOGGER.info("Received message {}", IOUtils.toString(inputstream));
                IOUtils.closeQuietly(inputstream);
            }
        } catch (IOException ex) {
            IOUtils.closeQuietly(serverSocket);
            LOGGER.error("Read message failed", ex);
        }
    }
}
