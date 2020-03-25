package com.adri.economy.amqp;

import com.adri.economy.dto.OperationMessageDTO;
import com.adri.economy.service.AccountBalanceService;
import com.adri.economy.service.AccountMonthStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class OperationMqListener {

    private final AccountBalanceService accountBalanceService;
    private final AccountMonthStatisticsService accountMonthStatisticsService;

    @RabbitListener(queues = "${rabbitmq.queueName}")
    public void listen(final OperationMessageDTO message) {
        log.debug("New operation {} for account {}", message.getOperationId(), message.getAccountId());

        try {
            accountBalanceService.updateAccountBalance(message.getOperationId());
            accountMonthStatisticsService.updateAccountStatistics(message.getOperationId());
        } catch (Exception ex){
            log.error("Error updating account balance of account {}", message.getAccountId(), ex);
        }
    }
}
