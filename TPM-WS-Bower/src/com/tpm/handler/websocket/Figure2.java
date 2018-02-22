package com.tpm.handler.websocket;

import org.json.simple.JSONObject;



public class Figure2 {
    private JSONObject json;
    public Figure2(JSONObject json) {
        this.json = json;
    }
	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}
	@Override
    public String toString() {
         return json.toJSONString();
    }
	
}

	
