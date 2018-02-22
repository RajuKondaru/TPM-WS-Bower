package com.tpm.handler.websocket;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.tpm.mobile.android.exe.api.appium.utils.AppiumProxy4;
@ServerEndpoint(value="/tpwsendpoint",  encoders = {FigureEncoder.class}, decoders = {FigureDecoder.class})
public class TPWSEndpoint {
    private static final Logger LOG = Logger.getLogger(TPWSEndpoint.class.getName());
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    @OnMessage
    public void onMessage(Figure figure, Session peer) throws IOException, EncodeException {
    	
        if (figure != null) {
        	LOG.info(figure.getJson().toString());
        	if(figure.getJson().containsKey("appiumTestExecuter")){
        		JsonValue testConfig = figure.getJson().get("appiumTestExecuter");
        		String jsonstr= testConfig.toString();
        		JsonObject jsonObject = Json.createReader(new StringReader(jsonstr)).readObject();
        		
        		new AppiumProxy4().startProxy(peer, jsonObject);
            } else if (figure.getJson().containsKey("status")) {
            	String status=	figure.getJson().get("status").toString();
            	 LOG.info(status);
    		
            	if(status.equals("false")){
            		if(peers.contains(peer)){
            			 peer.getBasicRemote().sendText("{\"connect\": \"true\"}");
            			 LOG.info("send to client {\"connect\": \"true\"}");
            		} 
            		
            		
            	}
            	
			}
        }
      
    }
    @OnOpen
    public void onOpen(Session peer) throws IOException, EncodeException {
        LOG.info("Connection opened ...");
        peers.add(peer);
    }
    @OnClose
    public void onClose(Session peer) {
        LOG.info("Connection closed ...");
        
        peers.remove(peer);
    }
}