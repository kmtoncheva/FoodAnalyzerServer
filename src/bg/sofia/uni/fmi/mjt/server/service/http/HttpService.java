package bg.sofia.uni.fmi.mjt.server.service.http;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;

public interface HttpService {
    String get(String url) throws ApiException;
}