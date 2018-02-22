package com.tpm.handler.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class FigureEncoder2 implements Encoder.Text<Figure2> {

	@Override
    public void init(EndpointConfig ec) {
       // System.out.println("init");
    }

    @Override
    public void destroy() {
        //System.out.println("destroy");
    }
    
	@Override
	public String encode(Figure2 figure) throws EncodeException {
		// TODO Auto-generated method stub
		return figure.getJson().toString();
	}
    
}