package com.iambadatplaying.data.state;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iambadatplaying.Starter;
import com.iambadatplaying.data.DataManager;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndOfGameStatsManager extends StateDataManager {

    private static final Pattern endOfGameStatsPattern = Pattern.compile("/lol-end-of-game/v1/eog-stats-block$");

    public EndOfGameStatsManager(Starter starter) {
        super(starter);
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected Matcher getURIMatcher(String uri) {
        return endOfGameStatsPattern.matcher(uri);
    }

    @Override
    protected void doUpdateAndSend(Matcher uriMatcher, String type, JsonElement data) {

    }

    @Override
    protected void doShutdown() {

    }

    @Override
    protected Optional<JsonObject> fetchCurrentState() {
        return Optional.empty();
    }


    @Override
    public String getEventName() {
        return null;
    }
}
