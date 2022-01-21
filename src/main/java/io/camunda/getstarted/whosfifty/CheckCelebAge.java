package io.camunda.getstarted.whosfifty;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Component
public class CheckCelebAge {

    public static String apiKey;

    @Value("${celebrityninjas.key}")
    public void setApiKey(String key){
        apiKey = key;
    }

    public CheckCelebAge() {

    }




    public CelebDetails getCelebDetails(String celebName) throws Exception {
        System.out.println("Making call for "+celebName);
        HttpResponse<String> response = get("https://api.celebrityninjas.com/v1/search?limit=10&name=" + celebName );

        if (response.statusCode() != 200) {

            // create incidence if Status code is not 200#
            System.out.println("Error Status Code: "+ response.statusCode());
            throw new Exception("Error from REST call, Response code: " + response.statusCode());

        } else {

            //Get the first celeb returned from the call
            JSONArray body = new JSONArray(response.body());
            String firstCelebFound = body.getJSONObject(0).toString();

            // Map the celeb to a local object
            ObjectMapper om = new ObjectMapper();
            CelebDetails celebDetails = om.readValue(firstCelebFound, CelebDetails.class);

            return celebDetails;

        }
    }

    public HttpResponse<String> get(String uri) throws Exception {

        uri = uri.replace(" ", "+");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("X-Api-Key", apiKey)
                .header("Content-Type", "application/json")
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response;
    }

}

