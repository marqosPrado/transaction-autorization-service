package com.marcosprado.transactionautorizationservice.application.usecase;

import com.marcosprado.transactionautorizationservice.application.command.CreateAccountCommand;
import com.marcosprado.transactionautorizationservice.domain.model.Account;
import com.marcosprado.transactionautorizationservice.domain.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateAccountUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateAccountUseCase.class);
    private final AccountRepository accountRepository;

    public CreateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void execute(CreateAccountCommand command) {
        if (accountRepository.existsById(command.id())) {
            log.info("Account already exists: {}", command.id());
            return;
        }

        Account account = new Account(
                command.id(),
                command.ownerId(),
                command.status(),
                command.createdAt()
        );

        accountRepository.save(account);
        log.info("Account created successfully: id={}, owner={}",
                account.getId(), account.getOwnerId());
    }
}
