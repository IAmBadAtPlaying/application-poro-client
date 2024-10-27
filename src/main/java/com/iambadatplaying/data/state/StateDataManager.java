package com.iambadatplaying.data.state;

import com.google.gson.JsonObject;
import com.iambadatplaying.Starter;
import com.iambadatplaying.data.BasicDataManager;
import com.iambadatplaying.data.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class StateDataManager extends BasicDataManager {

    protected JsonObject                currentState    = null;

    protected StateDataManager(Starter starter) {
        super(starter);
    }

    public Optional<JsonObject> getCurrentState() {
        if (!initialized) {
            log("Not initialized, wont fetch current state", Starter.LOG_LEVEL.ERROR);
            return Optional.empty();
        }
        if (currentState != null) return Optional.of(currentState);
        Optional<JsonObject> newState = fetchCurrentState();
        newState.ifPresent(jsonObject -> currentState = jsonObject);
        return newState;
    }

    public void setCurrentState(JsonObject currentState) {
        this.currentState = currentState;
    }

    protected abstract Optional<JsonObject> fetchCurrentState();

    @Override
    public void sendCurrentState() {
        getCurrentState().ifPresent(
                state ->
                        starter.getServer().sendToAllSessions(DataManager.getEventDataString(getEventName(), state))
        );
    }

    @Override
    public void shutdown() {
        if (!initialized) return;
        initialized = false;
        doShutdown();
        currentState = null;
    }

    public void resetState() {
        if (!initialized) {
            log("Not initialized, wont have any effect", Starter.LOG_LEVEL.WARN);
            return;
        }
        currentState = new JsonObject();
        sendCurrentState();
    }

    public abstract String getEventName();
}
