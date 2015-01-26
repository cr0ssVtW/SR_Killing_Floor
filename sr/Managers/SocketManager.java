package sr.Managers;

import java.util.logging.Level;
import sr.SRKF;

/**
 *
 * @author Cross
 *
 */

public final class SocketManager 
{
    public SRKF plugin;
    public SocketServer socketServer;
    public Thread socketThread;
    public int socketPort;
    public String socketAddress;
    
    public SocketManager (SRKF plugin)
    {
        this.plugin = plugin;
        startServer();
    }
    
    public void startServer()
    {
        this.socketAddress = SRKF.config.getString("ServerAddress");
        this.socketPort = SRKF.config.getInt("socketPort");
        
        try
        {
            this.socketServer = new SocketServer(this, this.socketAddress, this.socketPort);
            this.socketThread = new Thread(this.socketServer);
            
            this.socketThread.start();
            
            SRKF.LOG.log(Level.INFO, "[SRKF] - The Socket Server Started");
        } catch (Exception e)
        {
            e.printStackTrace();
            SRKF.LOG.log(Level.WARNING, "[SRKF] - Unable to start the Socket Server!");
        }
    }
}
