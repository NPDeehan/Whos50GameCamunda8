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

    private static String apiKey;

    @Value("${api-ninjas.key}")
    public void setApiKey(String key){
        apiKey = key;
    }

    public CheckCelebAge() {}

    public CelebDetails getCelebDetails(String celebName) throws Exception {
        String url = "https://api.api-ninjas.com/v1/celebrity?limit=10&name=" + celebName.replace(" ", "+");

        HttpResponse<String> response = get(url);

        if (response.statusCode() != 200) {

            // create incident if Status code is not 200#
            System.out.println("Error Status Code: "+ response.statusCode());
            throw new Exception("Error from REST call, Response code: " + response.statusCode());

        } else {

            //Get the first celeb returned from the call
            JSONArray body = new JSONArray(response.body());

            //
            if (body.isNull(0)){
                return null;
            } else {
                String firstCelebFound = body.getJSONObject(0).toString();

                // Map the celeb to a local object
                ObjectMapper om = new ObjectMapper();
                CelebDetails celebDetails = om.readValue(firstCelebFound, CelebDetails.class);
                return celebDetails;
            }

        }
    }

    private HttpResponse<String> get(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("X-Api-Key", apiKey)
                .header("Content-Type", "application/json")
                .uri(URI.create(uri))
                .build();

        System.out.println("Fetching URI: " + uri);
        HttpResponse<String> response = client
            .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Got status code: " + response.statusCode());
        System.out.println("=== Body:");
        System.out.println(response.body());
        System.out.println("=========");
        return response;
    }

}

