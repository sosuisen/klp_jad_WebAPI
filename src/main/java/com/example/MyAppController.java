package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MyAppController {

	@FXML
	private TextArea textArea;

	public void initialize() {
		try {
			var url = "https://geoapi.heartrails.com/api/json?method=searchByPostal&postal=0010010";
			/*
			var url = "https://geoapi.heartrails.com/api/json?method=suggest&matching=like&keyword="
					+ URLEncoder.encode("京都市南区", "UTF-8");
			*/
			var client = HttpClient.newHttpClient();
			var request = HttpRequest.newBuilder()
					.uri(URI.create(url))
					.build();

			HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());
			String body = res.body(); 
			// bodyに含まれる日本語は次のようなUnicodeエスケープシーケンスなので注意："\u672d\u5e4c\u5e02\u5317\u533a"
			textArea.setText(body + "\n"); 
			
			// Unicodeエスケープシーケンスは別途プログラムを書いて変換することもできますが、
			// fromJsonのほうで変換して、読める文字にしてくれます。
			var gson = new Gson();
			HeartRails hr = gson.fromJson(body, HeartRails.class);
			hr.response.location.forEach(loc -> {
				textArea.appendText(loc.getPrefecture() + loc.getCity() + "\n");
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
