package com.example;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MyAppController {
	@FXML
	private TextArea textArea;

	@FXML
    private TextField inputField;

	public void initialize() {
		inputField.setOnAction(e -> {
			var keyword = inputField.getText(); 
			try {
				// var url = "https://geoapi.heartrails.com/api/json?method=searchByPostal&postal=0010010";

				var url = "https://geoapi.heartrails.com/api/json?method=suggest&matching=like&keyword="
						+ URLEncoder.encode(keyword, "UTF-8");

				var client = HttpClient.newHttpClient();
				var request = HttpRequest.newBuilder()
						.uri(URI.create(url))
						.build();

				HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
				String body = res.body();
				// bodyに含まれる日本語は次のようなUnicodeエスケープシーケンスなので注意："\u672d\u5e4c\u5e02\u5317\u533a"
				// textArea.setText(body + "\n");

				// Unicodeエスケープシーケンスは別途プログラムを書いて変換することもできますが、
				// fromJsonのほうで変換して、読める文字にしてくれます。
				var gson = new Gson();

				HeartRails hr = gson.fromJson(body, HeartRails.class);
				hr.getResponse().getLocation().forEach(loc -> {
					textArea.appendText(loc.getPrefecture() + loc.getCity() + loc.getTown() + "\n");
				});

				/*
				record LocationRecord(String prefecture, String city, String town) {};
				record ResponseRecord(List<LocationRecord> location) {};
				record HeartRailsRecord(ResponseRecord response) {};
				HeartRailsRecord hr = gson.fromJson(body, HeartRailsRecord.class);
				hr.response.location.forEach(loc -> {
					textArea.appendText(loc.prefecture + loc.city + "\n");
				});
				*/
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
}
