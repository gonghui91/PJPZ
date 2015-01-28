package com.pjpz.model;

import com.google.gson.Gson;

public class BaseModel {
	public String toJson() {
		return new Gson().toJson(this);
	}
}
