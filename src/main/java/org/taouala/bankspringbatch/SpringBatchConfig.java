package org.taouala.bankspringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.taouala.bankspringbatch.dao.BankTransaction;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.api.chunk.ItemReader;
import javax.batch.api.chunk.ItemWriter;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory<BankTransaction> stepBuilderFactory;
    @Autowired
    private ItemReader<BankTransaction> itemReader;
    @Autowired
    private ItemWriter<BankTransaction, BankTransaction> itemWriter;
    @Autowired
    private ItemProcessor<BankTransaction, BankTransaction> itemProcessor;

    @Bean
    public Job myJob() {
        Step step = stepBuilderFactory.get("ETL-Transaction-File-Load")
                .<BankTransaction, BankTransaction>chunk(100)
                .reader(itemReader)
                .writer(itemWriter)
                .processor(itemProcessor)
                .build();
        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader<BankTransaction> reader(@Value("${inputFile}") Resource resource) {
        FlatFileItemReader<BankTransaction> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setName("CSV-READER");
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setResource(resource);
        fileItemReader.setLineMapper(mapper());
        return fileItemReader;
    }

    @Bean
    public LineMapper<BankTransaction> mapper() {
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer= new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","accountID","strDate","transactionType","amount");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper =new BeanWrapperFieldSetMapper<>();
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}
