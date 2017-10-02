package com.example.adm.myapplication;

import java.util.List;


public class DTO {

    private List<String> items;

    DTO(){}

    DTO(List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {return items;}

    public void setItems(List<String> items) {this.items = items;}
}
