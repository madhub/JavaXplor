package com.msxplor.demos;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpRequest.BodyPublisher;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
/**
 * https://openjdk.org/groups/net/httpclient/recipes.html
 */
public class HttpClientExamples {

        private static final HttpClient httpClient = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .connectTimeout(Duration.ofSeconds(10))
                        .build();

        public static void usingHttpClientSynchronous() throws IOException, InterruptedException {
                HttpClient httpClient = HttpClient.newBuilder()
                                .version(HttpClient.Version.HTTP_1_1)
                                .connectTimeout(Duration.ofSeconds(10))
                                .build();

                HttpRequest request = HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create("https://postman-echo.com/get"))
                                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                                .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                HttpHeaders headers = response.headers();
                headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

                System.out.println(response.statusCode());
                System.out.println(response.body());
        }

        public static void usingHttpClientASynchronous() throws IOException, InterruptedException {

                HttpRequest request = HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create("https://postman-echo.com/get"))
                                .setHeader("User-Agent", "Java HttpClient Bot")
                                .build();

                CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request,
                                HttpResponse.BodyHandlers.ofString());

                String result = "";
                try {
                        result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
                } catch (ExecutionException | TimeoutException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                System.out.println(result);
        }

        public static void usingHttpClientMultipleConcurrentAsynchronous()
                        throws IOException, InterruptedException, URISyntaxException {

                List<URI> listOfUri = Arrays.asList(
                                new URI("https://httpbin.org/get?name=Chandler"),
                                new URI("https://httpbin.org/get?name=Ross"),
                                new URI("https://httpbin.org/get?name=Monika"));

                List<CompletableFuture<String>> result = listOfUri.stream()
                                .map(url -> httpClient.sendAsync(
                                                HttpRequest.newBuilder(url)
                                                                .GET()
                                                                .setHeader("User-Agent", "Java HttpClient Bot")
                                                                .build(),
                                                HttpResponse.BodyHandlers.ofString())
                                                .thenApply(response -> response.body()))
                                .collect(Collectors.toList());

                for (CompletableFuture<String> future : result) {
                        try {
                                System.out.println(future.get());
                        } catch (ExecutionException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                }

        }

        public static void usingHttpClientWithCustomExecutor() {

                // custom executor
                ExecutorService executorService = Executors.newFixedThreadPool(5);

                HttpClient httpClient = HttpClient.newBuilder()
                                .executor(executorService)
                                .version(HttpClient.Version.HTTP_2)
                                .connectTimeout(Duration.ofSeconds(10))
                                .build();
        }
        public static void usingHttpClientWithPos() throws IOException, InterruptedException {
                 // form parameters
                Map<Object, Object> data = new HashMap<>();
                data.put("username", "abc");
                data.put("password", "123");
                data.put("custom", "secret");
                data.put("ts", System.currentTimeMillis());

                HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(data))
                .uri(URI.create("https://httpbin.org/post"))
                .setHeader("User-Agent", "Java HttpClient Bot") // add request header
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());
        }

        public static void usingHttpClient2() {
                HttpClient httpClient = HttpClient.newBuilder()
                                .version(HttpClient.Version.HTTP_2)
                                .followRedirects(HttpClient.Redirect.NORMAL)
                                .connectTimeout(Duration.ofSeconds(20))
                                .proxy(ProxySelector.of(new InetSocketAddress("proxy.server.com", 80)))
                                .authenticator(Authenticator.getDefault())
                                .build();
        }

         // Sample: 'password=123&custom=secret&username=abc&ts=1570704369823'
    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
