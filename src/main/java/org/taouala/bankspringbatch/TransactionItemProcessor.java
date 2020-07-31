package org.taouala.bankspringbatch;

import org.taouala.bankspringbatch.dao.BankTransaction;

import javax.batch.api.chunk.ItemProcessor;
import java.text.SimpleDateFormat;

public class TransactionItemProcessor implements ItemProcessor<BankTransaction, BankTransaction> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm")
    @Override
    public Object processItem(Object o) throws Exception {
        BankTransaction bankTransaction=(BankTransaction) o;
        bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
        return bankTransaction;
    }
}
