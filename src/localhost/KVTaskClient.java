package localhost;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String API_TOKEN;
    private final String url;
    HttpClient httpClient;
    public KVTaskClient(String url)  {
        this.url = url;

        try {
            URI registerUrl = new URI(url + "/register");
            httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest
                    .newBuilder()
                    .uri(registerUrl)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );
            API_TOKEN = response.body();
        } catch (URISyntaxException | InterruptedException | IOException e) {
            System.out.println("При запуске KVTaskClient произошла ошибка.");
            e.printStackTrace();
        }
    }

    public void put(String key, String json) {
        try {
            URI saveUrl = new URI(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
            httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest
                    .newBuilder()
                    .uri(saveUrl)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println("Ошибка при сохранении задачи.");
        }
    }

    public String load(String key) {
        HttpResponse<String> httpResponse = null;
        try {
        URI loadUri = new URI(url + "/load/" + key + "?API_TOKEN=" + API_TOKEN);
        httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(loadUri)
                .GET()
                .build();
        httpResponse = httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString()
        );
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println("Ошибка при сохранении задачи.");
        }

        return httpResponse.body();
    }
}
