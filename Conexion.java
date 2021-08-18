import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion implements IData {

    private Connection con;
    private boolean connected;
    
    public Connection getCon() {
        return con;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public Conexion() {
        this.con = null;
        this.connected = false;
    }

    public Conexion(String url) {
        this.con = null;
        this.connected = false;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.con = DriverManager.getConnection("jdbc:sqlite:" + URL);
            this.connected = true;
        } catch (Exception e) {
            connected = false;
        }
    }
 
    public void disconnect() {
        if(this.connected){
            this.connected = false;
            try {
                this.con.close();
            } catch (Exception e) {
                this.con = null;
            }
        }
    }   
}
