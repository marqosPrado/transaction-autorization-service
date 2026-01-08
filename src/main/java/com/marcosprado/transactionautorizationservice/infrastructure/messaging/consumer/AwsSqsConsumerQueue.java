package com.marcosprado.transactionautorizationservice.infrastructure.messaging.consumer;

import com.marcosprado.transactionautorizationservice.application.command.CreateAccountCommand;
import com.marcosprado.transactionautorizationservice.application.usecase.CreateAccountUseCase;
import com.marcosprado.transactionautorizationservice.infrastructure.messaging.dto.CreatedAccountMessage;
import com.marcosprado.transactionautorizationservice.infrastructure.messaging.mapper.AccountMessageMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AwsSqsConsumerQueue {

    private static final Logger log = LoggerFactory.getLogger(AwsSqsConsumerQueue.class);

    private final CreateAccountUseCase createAccountUseCase;
    private final AccountMessageMapper messageMapper;

    public AwsSqsConsumerQueue(CreateAccountUseCase createAccountUseCase,
                               AccountMessageMapper messageMapper) {
        this.createAccountUseCase = createAccountUseCase;
        this.messageMapper = messageMapper;
    }

    @SqsListener("${aws.sqs.queue-name}")
    public void receiveMessage(CreatedAccountMessage message) {
        try {
            log.info("Received message from SQS: {}", message);

            CreateAccountCommand command = messageMapper.toCommand(message);
            createAccountUseCase.execute(command);

        } catch (IllegalArgumentException e) {
            log.error("Invalid message format: {}", message, e);
            throw e;
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            throw e;
        }
    }
}
