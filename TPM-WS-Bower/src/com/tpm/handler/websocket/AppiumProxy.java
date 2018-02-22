package com.tpm.handler.websocket;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.parser.ParseException;
@ServerEndpoint(value="/wd/hub/*")
public class AppiumProxy {
  
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    
	@OnMessage
    public void onMessage(String message, final Session peer) throws IOException, EncodeException, ParseException {
    	System.out.println(message);
        
      
    }
    @OnOpen
    public void onOpen(Session peer) throws IOException, EncodeException {
    	System.out.println("Connection opened ...");
        peers.add(peer);
    }
    @OnClose
    public void onClose(Session peer) {
    	System.out.println("Connection closed ...");
        
        peers.remove(peer);
    }
    
}