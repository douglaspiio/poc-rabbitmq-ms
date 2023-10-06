package br.com.microservico.operacoes.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class OperacaoAMQPConfiguration {
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
    	ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return  rabbitTemplate;
    }

    @Bean
    public Queue filaOperacaoCadastrada() {
        return QueueBuilder
                .nonDurable("operacao.realizada")
                .build();
    }
    
    @Bean
    public Queue filaPagamentoConcluido() {
        return QueueBuilder
                .nonDurable("operacoes.finalizadas")
                .build();
    }
    
    @Bean
    public Queue filaPagamentoParcial() {
        return QueueBuilder
                .nonDurable("operacoes.paga-parcialmente")
                .build();
    }
    
    @Bean
    public Queue filaPagamentoRecusado() {
        return QueueBuilder
                .nonDurable("operacao.recusadas")
                .build();
    }

//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return ExchangeBuilder
//                .fanoutExchange("pagamentos.ex")
//                .build();
//    }

//    @Bean
//    public Binding bindPagamentoOperacao(FanoutExchange fanoutExchange) {
//        return BindingBuilder
//                .bind(filaDetalhesOperacao())
//                .to(fanoutExchange());
//    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

}
