package test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.tpm.mobile.log.LogServiceClient;

public class MongoConnection {
	private static Mongo mongo;
	private static String host;
	private static Integer port;
	private static String dbName;
	private static DB db;
	
	public MongoConnection(){
		Properties prop = new Properties();
		try {
			prop.load(LogServiceClient.class.getClassLoader()
					.getResourceAsStream("config.properties"));
			host = prop.getProperty("MONGODB_HOST");
			port = Integer.parseInt(prop.getProperty("MONGODB_PORT")) ;
			dbName = prop.getProperty("MONGODB_NAME");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public DB getDb() throws UnknownHostException {
        if (mongo == null){
        	mongo = new Mongo(host, port);
        	db = mongo.getDB(dbName);
        	//System.out.println(db.getName()); 
        }
            
        return db;
    }
    public void close() throws UnknownHostException {
        if (mongo != null){
        	mongo.close(); 
        }
    }
}
