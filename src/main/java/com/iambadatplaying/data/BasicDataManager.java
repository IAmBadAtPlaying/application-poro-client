package com.iambadatplaying.data;

import com.google.gson.JsonElement;
import com.iambadatplaying.Starter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BasicDataManager {
    public static final String UPDATE_TYPE_DELETE = "Delete";
    public static final String UPDATE_TYPE_CREATE = "Create";
    public static final String UPDATE_TYPE_UPDATE = "Update";
    protected boolean initialized = false;
    protected Starter starter;

    protected BasicDataManager(Starter starter) {
        this.starter = starter;
    }

    public void init() {
        if (initialized) return;
        doInitialize();
        initialized = true;
        log("Initialized", Starter.LOG_LEVEL.INFO);
    }

    protected abstract void doInitialize();

    protected abstract Matcher getURIMatcher(String uri);

    protected abstract void doUpdateAndSend(Matcher uriMatcher, String type, JsonElement data);

    public void update(String uri, String type, JsonElement data) {
        if (!initialized) {
            return;
        }

        Matcher uriMatcher = getURIMatcher(uri);
        if (!uriMatcher.matches()) return;
        doUpdateAndSend(uriMatcher, type, data);
    }

    public void shutdown() {
        if (!initialized) return;
        initialized = false;
        doShutdown();
    }

    protected abstract void doShutdown();

    public abstract void sendCurrentState();

    protected void log(Object o, Starter.LOG_LEVEL level) {
        starter.log(this.getClass().getSimpleName() + ": " + o, level);
    }

    protected void log(Object o) {
        log(o, Starter.LOG_LEVEL.DEBUG);
    }
}
