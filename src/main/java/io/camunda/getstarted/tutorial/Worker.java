package io.camunda.getstarted.tutorial;

import java.util.Map;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.EnableZeebeClient;

@SpringBootApplication
@EnableZeebeClient
public class Worker {

    public static void main(String[] args) {
        SpringApplication.run(Worker.class, args);
    }

    @JobWorker(type = "CheckResults")
    public Map<String, Object> orchestrateResults(final ActivatedJob job) {

        Map<String, Object> variables = job.getVariablesAsMap();

        //Date values
        int temperature = (int) variables.get("temperature");
        int pressure    = (int) variables.get("pressure");
        int pulse       = (int) variables.get("pulse");

        // Analyze data
        boolean patientStatus = true;
        if(temperature < 364 || temperature > 370) patientStatus = false;
        if(pressure < 84 || pressure > 120) patientStatus = false;
        if(pulse < 60 || pulse > 100) patientStatus = false;

        // Send Results
        variables.put("ServiceResponse", patientStatus);
        return variables;
    }

    @JobWorker(type = "ResultsOK")
        public Map<String, Object> orchestrateResultsOK(final ActivatedJob job) {

        Map <String, Object> variables = job.getVariablesAsMap();
        System.out.println("Everything is ok!");
        return variables;
    }

    @JobWorker(type = "ResultsBAD")
        public Map<String, Object> orchestrateResultsBAD(final ActivatedJob job) {

        Map <String, Object> variables = job.getVariablesAsMap();
        System.out.println("Bad symptoms! Medical care required!");
        return variables;
    }

    @JobWorker(type = "FinalTask")
        public Map<String, Object> orchestrateFinalTask(final ActivatedJob job) {

        Map <String, Object> variables = job.getVariablesAsMap();
        System.out.println(variables.toString());
        System.out.println("FinalTask activated!");
        return variables;
    }

}
