package org.taouala.bankspringbatch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BankRestController {

    private JobLauncher jobLauncher;
    private Job job;

    public BankRestController(Job job, JobLauncher jobLauncher){
        this.job=job;
        this.jobLauncher=jobLauncher;
    }

    @RequestMapping("loadData")
    public BatchStatus load() throws Exception{

        Map<String, JobParameter> map = new HashMap();
        map.put("time",new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(map);
        JobExecution jobExecution=jobLauncher.run(job,jobParameters);
        while (jobExecution.isRunning()){
            System.out.println("....");
        }
        return jobExecution.getStatus();
    }
}
