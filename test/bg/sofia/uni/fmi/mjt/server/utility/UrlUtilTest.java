package bg.sofia.uni.fmi.mjt.server.utility;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UrlUtilTest {
    @Test
    void testBuildUrlWithMultipleParts() {
        String result = UrlUtil.buildUrl("/search", "?q=java", "&sort=desc");
        assertEquals(BASE_URL + "/search?q=java&sort=desc", result);
    }

    @Test
    void testBuildUrlWithNoParts() {
        String result = UrlUtil.buildUrl();
        assertEquals(BASE_URL, result);
    }

    @Test
    void testEncodeTokensWithSpecialCharacters() {
        String[] input = {"java", "open ai", "test&check"};
        String result = UrlUtil.encodeTokens(input);
        System.out.println(result);

        assertTrue(result.contains("open%20ai"));
        assertTrue(result.contains("test%26check"));
    }

    @Test
    void testEncodeTokensWithEmptyArray() {
        String result = UrlUtil.encodeTokens(new String[0]);
        assertEquals("", result);
    }
}
