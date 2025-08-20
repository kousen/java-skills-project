package com.oreilly.webservices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Transaction configuration for in-memory operations.
 * <p>
 * Since we're using ConcurrentHashMap for storage (not a database),
 * we use ResourcelessTransactionManager, which provides transaction semantics
 * without actual database transaction support.
 * <p>
 * This enables @Transactional annotations to work for demonstrating
 * transaction boundaries and service layer architecture patterns.
 */
@Configuration
public class TransactionConfig {
    
    /**
     * Provides a simple transaction manager for in-memory operations.
     * This enables @Transactional annotations without requiring a database.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new AbstractPlatformTransactionManager() {
            @Override
            @NonNull
            protected Object doGetTransaction() throws TransactionException {
                return new Object();
            }
            
            @Override
            protected void doBegin(@NonNull Object transaction, @NonNull TransactionDefinition definition) 
                    throws TransactionException {
                // No-op for in-memory operations
            }
            
            @Override
            protected void doCommit(@NonNull DefaultTransactionStatus status) throws TransactionException {
                // No-op for in-memory operations  
            }
            
            @Override
            protected void doRollback(@NonNull DefaultTransactionStatus status) throws TransactionException {
                // No-op for in-memory operations
            }
        };
    }
}