package com.adri.economy.kafka;

import com.adri.economy.kafka.model.OperationKafka;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.username}")
    private String kafkaUser;

    @Value(value = "${kafka.password}")
    private String kafkaPass;

    @Value(value = "${kafka.group.balance}")
    private String kafkaBalanceGroup;

    @Value(value = "${kafka.group.stats}")
    private String kafkaStatsGroup;

    public ConsumerFactory<Long, OperationKafka> balanceConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaUser+"-"+kafkaBalanceGroup);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //CloudKarafka config
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, kafkaUser, kafkaPass);

        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("sasl.jaas.config", jaasCfg);

        return new DefaultKafkaConsumerFactory<>(props, new LongDeserializer(), new JsonDeserializer<>(OperationKafka.class, false));
    }

    public ConsumerFactory<Long, OperationKafka> statsConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaUser+"-"+kafkaStatsGroup);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //CloudKarafka config
        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, kafkaUser, kafkaPass);

        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("sasl.jaas.config", jaasCfg);

        return new DefaultKafkaConsumerFactory<>(props, new LongDeserializer(), new JsonDeserializer<>(OperationKafka.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, OperationKafka> balanceKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, OperationKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(balanceConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, OperationKafka> statsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, OperationKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(statsConsumerFactory());
        return factory;
    }

}
