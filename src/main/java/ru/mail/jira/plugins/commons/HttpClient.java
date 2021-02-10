package ru.mail.jira.plugins.commons;

import kong.unirest.*;

import java.util.Map;

public class HttpClient {

    public static UnirestInstance getClient() {
        return Unirest.primaryInstance();
    }

    public static GetRequest get(String url, Object... params) {
        return get(CommonUtils.formatUrl(url, params));
    }

    public static GetRequest get(String url) {
        return Unirest.get(url);
    }

    public static HttpRequestWithBody post(String url, Object... params) {
        return post(CommonUtils.formatUrl(url, params));
    }

    public static HttpRequestWithBody post(String url) {
        return Unirest.post(url);
    }

    public static HttpRequestWithBody put(String url, Object... params) {
        return put(CommonUtils.formatUrl(url, params));
    }

    public static HttpRequestWithBody put(String url) {
        return Unirest.put(url);
    }

    public static HttpRequestWithBody delete(String url, Object... params) {
        return delete(CommonUtils.formatUrl(url, params));
    }

    public static HttpRequestWithBody delete(String url) {
        return Unirest.delete(url);
    }

    public <T> HttpResponse<T> get(String url, Map<String, String> headers, Integer connectTimeout, Class<T> c) {
        return Unirest.get(url).headers(headers).connectTimeout(connectTimeout).asObject(c);
    }

    public HttpResponse<String> get(String url, Map<String, String> headers, Integer connectTimeout) {
        return Unirest.get(url).headers(headers).connectTimeout(connectTimeout).asString();
    }

    public HttpResponse<JsonNode> getJson(String url) {
        return Unirest.get(url).asJson();
    }

    public <T> HttpResponse<T> getObject(String url, Class<T> c) {
        return Unirest.get(url).asObject(c);
    }

    public HttpResponse post(String url, Object body, Integer connectionTimeout) {
        return Unirest.post(url).body(body).connectTimeout(connectionTimeout).asEmpty();
    }

    public HttpResponse put(String url, Object body, Integer connectionTimeout) {
        return Unirest.put(url).body(body).connectTimeout(connectionTimeout).asEmpty();
    }

    public HttpResponse delete(String url, Object body, Integer connectionTimeout) {
        return Unirest.put(url).body(body).connectTimeout(connectionTimeout).asEmpty();
    }
}

