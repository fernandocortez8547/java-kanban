package localhost;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVTaskClient {
    private final String apiToken;
    private final String url;
    private HttpClient httpClient;

    public KVTaskClient(String url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();

        try {
            URI registerUrl = URI.create(url + "/register");
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(registerUrl)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            apiToken = response.body();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("При запуске KVTaskClient произошла ошибка.");
        }
    }

    public void put(String key, String json) {
        try {
            URI saveUrl = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest httpRequest = HttpRequest
                    .newBuilder()
                    .uri(saveUrl)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("При сохранении задачи произошла ошибка.");
        }
    }

    public String load(String key) {
        HttpResponse<String> response = null;
        try {
            URI loadUri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(loadUri)
                    .GET()
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("При загрузке задач произошла ошибка.");
        }

        return response.body();
    }
}
