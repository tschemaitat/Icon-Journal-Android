package com.example.habittracker.Structs;

public class PayloadOption {
    private Object payload;
    private String option;
    public PayloadOption(String option, Object payload){
        this.payload = payload;
        this.option = option;
    }

    public Object getPayload() {
        return payload;
    }

    public String getString() {
        return option;
    }
}
