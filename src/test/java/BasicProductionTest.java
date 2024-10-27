import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iambadatplaying.Starter;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class BasicProductionTest {

    private static final String GIT_API_URL = "https://api.github.com/repos/IAmBadAtPlaying/application-poro-client/releases/latest";

    private final static String GIT_KEY_TAG_NAME             = "tag_name";

    private final static String  REGEX_VERSION   = "v(\\d+)\\.(\\d+)\\.(\\d+)";
    private final static Pattern PATTERN_VERSION = Pattern.compile(REGEX_VERSION);



    @Test
    public void testBasicProductionReadiness() {
        assertFalse("Starter isDev should be set to false",Starter.isDev);
    }

    @Test
    public void testVersionNumberIncrease() {
        JsonObject apiData = fetchGithubApiInfo();
        if (apiData == null) {
            // Maybe there is no release yet
            assertEquals(0, Starter.VERSION_PATCH);
            assertEquals(1, Starter.VERSION_MINOR);
            assertEquals( 0, Starter.VERSION_MAJOR);
            return;
        }
        assertTrue(apiData.has(GIT_KEY_TAG_NAME));
        String latestVersion = apiData.get(GIT_KEY_TAG_NAME).getAsString();
        Matcher matcher = PATTERN_VERSION.matcher(latestVersion);
        assertTrue("Version Pattern doesnt match" ,matcher.matches());

        final int latestMajor = Integer.parseInt(matcher.group(1));
        final int latestMinor = Integer.parseInt(matcher.group(2));
        final int latestPatch = Integer.parseInt(matcher.group(3));

        assertTrue("Version should have updated", didVersionUpdate(latestMajor, latestMinor, latestPatch));
    }

    public static boolean didVersionUpdate(
            int lastMaj,
            int lastMin,
            int lastPatch
    ) {
        if (Starter.VERSION_MAJOR > lastMaj) return true;
        if (Starter.VERSION_MAJOR < lastMaj) return false;
        if (Starter.VERSION_MINOR > lastMin) return true;
        if (Starter.VERSION_MINOR < lastMin) return false;
        return Starter.VERSION_PATCH > lastPatch;
    }

    private static JsonObject fetchGithubApiInfo() {
        try {
            URL url = new URL(GIT_API_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }
            return getResponseBodyAsJsonObject(connection);
        } catch (Exception e) {
            return null;
        }
    }

    private static JsonObject getResponseBodyAsJsonObject(HttpsURLConnection conn) {
        String resp = handleStringResponse(conn);
        if (resp == null) {
            return null;
        }
        return JsonParser.parseString(resp).getAsJsonObject();
    }

    private static String handleStringResponse(HttpURLConnection conn) {
        String resp = null;
        try {
            if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
                resp = inputStreamToString(conn.getInputStream());
            } else {
                resp = inputStreamToString(conn.getErrorStream());
            }
            conn.disconnect();
        } catch (Exception e) {
            return null;
        }
        return resp;
    }

    private static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }
}
