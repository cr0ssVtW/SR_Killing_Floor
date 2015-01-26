package sr.Managers;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import sr.SRKF;

/**
 *
 * @author Cross
 */
public class SocketServer extends Thread
{
    SocketManager plugin;
    
    private int port;
    private String host;
    private ServerSocket server;
    
    public SocketServer(SocketManager plugin, String host, int port)
    {
        this.host = host;
        this.port = port;
        this.plugin = plugin;
    }
    
    @Override
    public void run()
    {
        try
        {
            SocketAddress sa = new InetSocketAddress(this.host, this.port);
            this.server = new ServerSocket();
            
            this.server.bind(sa);
            
            SRKF.LOG.log(Level.INFO, "[SRKF] - Socket Server {0} accepting connections on port {1}", new Object[]{this.host, this.port});
            
            while (true)
            {
                try
                {
                    Socket socket = this.server.accept();
                    new ConnMonitor(socket, this.plugin);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
