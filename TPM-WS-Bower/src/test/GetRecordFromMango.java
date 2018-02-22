package test;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class GetRecordFromMango {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("mobile");
		DBCollection collection = db.getCollection("manualtestcase.files");
	
		
		BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("testcaseName", "Insta_test01");
	    whereQuery.put("deviceId", "TA93300GXV");
	    whereQuery.put("AppId", "3");
	    whereQuery.put("UserID", "1");
	    
	  
	    DBCursor cursor = collection.find(whereQuery);
	    while (cursor.hasNext()) {
	        System.out.println(cursor.next());
	    }
	    mongo.close();
	}

}
