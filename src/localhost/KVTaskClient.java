package localhost;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String API_TOKEN;
    private String url;
    private final KVServer kvServer = new KVServer();
    HttpClient httpClient;
    public KVTaskClient(String url) throws IOException, InterruptedException, URISyntaxException {
        //по сути должна быть отправка запроса на чистый адрес
        //с последующим добавлением register
        this.url = url;
        URI registerUrl = new URI(url + "/register");

        kvServer.start();
        httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(registerUrl).GET().build();
        HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        API_TOKEN = response.body().toString();
    }

    public void put(String key, String json) throws URISyntaxException, IOException, InterruptedException {
        URI saveUrl = new URI(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN);
        httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(saveUrl)
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws URISyntaxException, IOException, InterruptedException {
        URI loadUri = new URI(url + "/load/" + key + "?API_TOKEN=" + API_TOKEN);
        httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(loadUri).GET().build();
        HttpResponse httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return httpResponse.body().toString();
    }
}
