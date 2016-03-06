package com.vgnary.nyt.thenewshour.eventBus;


public class TextSizeEvent {
    private float smallTextsize;
    private float largeTextsize;

    public TextSizeEvent(float smallTextsize, float largeTextSize) {
        this.smallTextsize = smallTextsize;
        this.largeTextsize = largeTextSize;
    }

    public float getSmallTextsize() {
        return smallTextsize;
    }

    public float getlargeTextsize() {
        return largeTextsize;
    }
}
