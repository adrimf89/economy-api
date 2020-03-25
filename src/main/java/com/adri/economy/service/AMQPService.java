package com.adri.economy.service;

import com.adri.economy.dto.OperationMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AMQPService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    public void sendOperationCreation(long operationId, long accountId){
        OperationMessageDTO msg = new OperationMessageDTO(operationId, accountId);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
        log.debug("Notify new operation {} for account {}", msg.getOperationId(), msg.getAccountId());
    }
}
