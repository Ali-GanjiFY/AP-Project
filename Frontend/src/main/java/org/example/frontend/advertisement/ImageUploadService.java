package org.example.frontend.advertisement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class ImageUploadService {

    private static final String UPLOAD_URL = "http://localhost:8080/api/uploads/images";

    private static final String BOUNDARY = "MyAppBoundary123456";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    // /uploads/abc.jpg
    public List<String> uploadImages(String token, List<File> files) {

        List<String> result = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            return result;
        }

        try {
            byte[] requestBody = buildRequestBody(files);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UPLOAD_URL))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "multipart/form-data; boundary=" + BOUNDARY)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                Type listType = new TypeToken<List<String>>() {}.getType();
                List<String> urls = gson.fromJson(response.body(), listType);
                if (urls != null) {
                    result = urls;
                }
            } else {
                System.out.println("آپلود عکس با خطا مواجه شد. کد وضعیت: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("خطا در آپلود عکس‌ها: " + e.getMessage());
        }

        return result;
    }

    private byte[] buildRequestBody(List<File> files) throws Exception {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        for (File file : files) {

            String header =
                    "--" + BOUNDARY + "\r\n" +
                            "Content-Disposition: form-data; name=\"files\"; filename=\"" + file.getName() + "\"\r\n" +
                            "Content-Type: " + guessContentType(file) + "\r\n" +
                            "\r\n";

            buffer.write(header.getBytes());

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            buffer.write(fileBytes);

            buffer.write("\r\n".getBytes());
        }

        buffer.write(("--" + BOUNDARY + "--\r\n").getBytes());

        return buffer.toByteArray();
    }

    private String guessContentType(File file) {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        } else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (name.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }
}