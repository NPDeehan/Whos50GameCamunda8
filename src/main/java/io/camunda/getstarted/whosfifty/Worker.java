package io.camunda.getstarted.whosfifty;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@SpringBootApplication
@EnableZeebeClient
public class Worker {

    public static void main(String[] args) {
        SpringApplication.run(Worker.class, args);
    }

    @ZeebeWorker(type = "CheckCelebAge")
    public void CheckCelebAge(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();
        String celebName = (String) variablesAsMap.get("celebName");

        CheckCelebAge checkAge = new CheckCelebAge();
        try {
            System.out.println("Going to check age for "+ celebName);
            CelebDetails celebDetails = checkAge.getCelebDetails(celebName);

            if(celebDetails != null) {
                celebName = celebDetails.name;

                int celebAge = celebDetails.age;
                HashMap<String, Object> variables = new HashMap<>();
                variables.put("age", celebAge);
                variables.put("celebName", celebName);
                variables.put("birthday", celebDetails.birthday);
                variables.put("response", celebName + " is " + celebAge + " Years old");

                System.out.println(celebName + " is " + celebAge + " Years old");

                client.newCompleteCommand(job.getKey())
                        .variables(variables)
                        .send()
                        .exceptionally((throwable -> {
                            throw new RuntimeException("Could not complete job", throwable);
                        }));
            } else {
                client.newThrowErrorCommand(job.getKey())
                        .errorCode("Celeb_not_found")
                        .errorMessage("I wasn't able to find that celeb")
                        .send()
                        .exceptionally((throwable -> {
                            throw new RuntimeException("Could not throw the BPMN Error Event", throwable);
                        }));
            }
        } catch(Exception e) {
            int retries = job.getRetries() - 1;

            e.printStackTrace();
            client.newFailCommand(job.getKey())
                .retries(retries)
                .send();
        }
    }
}
