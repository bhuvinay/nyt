package com.vgnary.nyt.thenewshour.eventBus;


public class ClearListEvent {
    private int listType;

    public ClearListEvent(int listType) {
        this.listType = listType;
    }

    public int getListType() {
        return listType;
    }


}
