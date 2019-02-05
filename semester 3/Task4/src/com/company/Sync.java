package com.company;

public class Sync {
    private Object sync;
    private boolean flag;

    public Sync() {
        sync = new Object();
        flag = true;
    }

    public Object getSync() {
        return sync;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }
}
