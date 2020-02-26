package me.mckd.life.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.mckd.life.Config;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReq {

    Config config;

    public HttpReq() {
        this.config = new Config();
    }

    public JsonObject get(String path, JsonObject obj) {
        String apiUrl = this.config.apiUrl;
        String urlString = apiUrl + path;
        /*
        System.out.println("================");
        System.out.println("URL:" + urlString);
        System.out.println("================");
        */
        HttpURLConnection conn = null;
        InputStream in = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(output.toString()).getAsJsonObject();
                json.addProperty("status", true);
                conn.disconnect();
                return json;
            }

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse("{status:false}").getAsJsonObject();
            // System.out.println(json);
            conn.disconnect();
            return json;

        } catch (IOException e) {
            e.printStackTrace();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse("{status:false}").getAsJsonObject();
            conn.disconnect();
            return json;
        }
    }

    public JsonObject get2(String path, JsonObject jsonObject) {
        HttpURLConnection conn;
        String apiUrl = this.config.apiUrl;
        String urlString = apiUrl + path;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(conn.getOutputStream()));
            if (jsonObject != null) {
                out.write(jsonObject.toString());
            } else {
                out.write("{}");
            }
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = in.readLine();
            String body = "";
            while (line != null) {
                body = body + line;
                line = in.readLine();
            }
            conn.disconnect();

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(body).getAsJsonObject();
            System.out.println(json);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse("{status:false}").getAsJsonObject();
            return json;
        }
    }

    public JsonObject post(String path, JsonObject jsonObject) {
        HttpURLConnection conn;
        String apiUrl = this.config.apiUrl;
        String urlString = apiUrl + path;
        System.out.println("================");
        System.out.println("URL:" + urlString);
        System.out.println("================");
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(conn.getOutputStream()));
            if (jsonObject != null) {
                out.write(jsonObject.toString());
            } else {
                out.write("{}");
            }
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = in.readLine();
            String body = "";
            while (line != null) {
                body = body + line;
                line = in.readLine();
            }
            conn.disconnect();

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(body).getAsJsonObject();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse("{status:false}").getAsJsonObject();
            return json;
        }
    }

    public JsonObject put(String path, JsonObject jsonObject) {
        HttpURLConnection conn;
        String apiUrl = this.config.apiUrl;
        String urlString = apiUrl + path;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(conn.getOutputStream()));
            if (jsonObject != null) {
                out.write(jsonObject.toString());
            } else {
                out.write("{}");
            }
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = in.readLine();
            String body = "";
            while (line != null) {
                body = body + line;
                line = in.readLine();
            }
            conn.disconnect();

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(body).getAsJsonObject();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse("{status:false}").getAsJsonObject();
            return json;
        }
    }

    public JsonObject delete(String path, JsonObject jsonObject) {
        HttpURLConnection conn;
        String apiUrl = this.config.apiUrl;
        String urlString = apiUrl + path;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(conn.getOutputStream()));
            if (jsonObject != null) {
                out.write(jsonObject.toString());
            } else {
                out.write("{}");
            }
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = in.readLine();
            String body = "";
            while (line != null) {
                body = body + line;
                line = in.readLine();
            }
            conn.disconnect();

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(body).getAsJsonObject();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse("{status:false}").getAsJsonObject();
            return json;
        }
    }

}
