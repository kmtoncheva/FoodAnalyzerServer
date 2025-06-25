package bg.sofia.uni.fmi.mjt.server.service.http;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiServiceUnavailableException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.InvalidApiRequestException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpServiceImplTest {
    private HttpClient client = mock();
    private HttpResponse<String> response = mock();

    private HttpService service = new HttpServiceImpl(client);

    @Test
    void testGetWithSuccessfulResponse() throws IOException, InterruptedException, ApiException {
        String expectedBody = "{\"food\":\"apple\"}";
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(expectedBody);
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(response);

        String result = service.get("https://example.com");

        assertEquals(expectedBody, result);
    }

    @Test
    void testGetWIthBadRequest() throws IOException, InterruptedException {
        when(response.statusCode()).thenReturn(400);
        when(response.body()).thenReturn("Bad Request");
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(response);

        assertThrows(InvalidApiRequestException.class, () -> service.get("https://example.com"));
    }

    @Test
    void testGetWithFoodItemNotFoundException() throws Exception {
        when(response.statusCode()).thenReturn(404);
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenReturn(response);

        assertThrows(FoodItemNotFoundException.class, () -> service.get("https://example.com"));
    }

    @Test
    void testGetWithApiServiceUnavailableException() throws Exception {
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenThrow(new IOException("Connection failed"));

        assertThrows(ApiServiceUnavailableException.class, () -> service.get("https://example.com"));
    }

    @Test
    void testGetWithInterruptedException() throws Exception {
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenThrow(new InterruptedException("Interrupted"));

        assertThrows(ApiServiceUnavailableException.class, () -> service.get("https://example.com"));
    }
}