package com.whzl.mengbi.eventbus.event;


/**
 * @author cliang
 * @date 2018.11.19
 */
public class GuardCountEvent {

    public int guardTotal;
    public int onLineCount;
    public int offLineCount;

    public GuardCountEvent(int guardTotal, int onLineCount, int offLineCount){
        this.guardTotal = guardTotal;
        this.onLineCount = onLineCount;
        this.offLineCount = offLineCount;
    }
}
