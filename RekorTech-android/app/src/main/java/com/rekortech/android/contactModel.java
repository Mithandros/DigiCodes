package com.rekortech.android;

import org.json.JSONObject;

public class contactModel{
    JSONObject contact;
        boolean sel;


public contactModel(JSONObject contact, boolean sel) {
        this.contact = contact;
        this.sel = sel;

        }

public void setContact(JSONObject contcat) {
        this.contact = contact;
        }

public JSONObject getContact() {
        return this.contact;
        }


public boolean isSel() {
        return sel;
        }

public void setSel(boolean sel) {
        this.sel = sel;
        }}