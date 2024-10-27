package com.iambadatplaying.data.map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.iambadatplaying.Starter;
import com.iambadatplaying.Util;
import com.iambadatplaying.data.BasicDataManager;
import com.iambadatplaying.lcuHandler.ConnectionManager;

import java.math.BigInteger;
import java.util.Optional;
import java.util.regex.Matcher;

public class SummonerIdToPuuidManager extends MapDataManager<BigInteger> {
    public static final String KEY_PUUID = "puuid";

    public SummonerIdToPuuidManager(Starter starter) {
        super(starter);
    }

    @Override
    public Optional<JsonObject> doLoad(BigInteger key) {
        log("Loading summonerIdToPuuid for key: " + key);
        JsonObject resp = ConnectionManager.getResponseBodyAsJsonObject(starter.getConnectionManager().buildConnection(ConnectionManager.conOptions.GET, "/lol-summoner/v1/summoners/" + key));
        if (resp == null) return Optional.empty();
        if (resp.has("errorCode")) return Optional.empty();
        JsonObject data = new JsonObject();
        Util.copyJsonAttributes(resp, data, KEY_PUUID);
        return Optional.of(data);
    }

    @Override
    public String getMapEventName() {
        return "";
    }

    @Override
    public String getKeyEventName() {
        return "";
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected Matcher getURIMatcher(String uri) {
        return Util.UNMATCHABLE_PATTERN.matcher(uri);
    }

    @Override
    protected void doUpdateAndSend(Matcher uriMatcher, String type, JsonElement data) {

    }

    @Override
    protected void doShutdown() {

    }

    @Override
    public Optional<JsonObject> getExternal(String key) {
        if (key == null) return Optional.empty();
        if (key.isEmpty()) return Optional.empty();
        try {
            BigInteger summonerId = new BigInteger(key);
            return get(summonerId);
        } catch (Exception e) {}
        return Optional.empty();
    }
}
