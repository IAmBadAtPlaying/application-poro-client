import com.iambadatplaying.lcuHandler.ConnectionManager;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BasicSecurityFunctions {

    @Test
    public void testSessionTokenProtected() {
        assertTrue("lol-session endpoint should be protected", ConnectionManager.isProtectedRessource("/lol-league-session/v1/league-session-token"));
    }
}
