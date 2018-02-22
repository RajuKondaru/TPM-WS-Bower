package com.tpm.handler.websocket;




import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class FigureDecoder2 implements Decoder.Text<Figure2> {
	@Override
    public Figure2 decode(String string) throws DecodeException {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(string);
        return  new Figure2(jsonObject);
    }

    @Override
    public boolean willDecode(String string) {
        try {
        	JSONValue.parse(string);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    
    }

    @Override
    public void init(EndpointConfig ec) {
        //System.out.println("init");
    }

    @Override
    public void destroy() {
        //System.out.println("destroy");
    }
    
}