package org.taouala.bankspringbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.taouala.bankspringbatch.dao.BankTransaction;
import org.taouala.bankspringbatch.dao.BankTransactionRepository;

import javax.batch.api.chunk.ItemWriter;
import java.io.Serializable;
import java.util.List;

public class BankTransactionItemWriter implements ItemWriter<BankTransaction> {

    @Autowired
    private BankTransactionRepository transactionRepository;

    @Override
    public void writeItems(List<Object> list) throws Exception {
        transactionRepository.saveAll(list);
    }

    @Override
    public void open(Serializable serializable) throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
