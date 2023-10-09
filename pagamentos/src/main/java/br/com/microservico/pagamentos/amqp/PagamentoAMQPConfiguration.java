package br.com.microservico.pagamentos.amqp;

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
public class PagamentoAMQPConfiguration {
    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

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
    public Queue filaPagamentoOperacao() {
        return QueueBuilder
                .nonDurable("pagamentos.operacao")
                .build();
    } 
    
    @Bean
    public Queue filaPagamentoRecusados() {
        return QueueBuilder
                .nonDurable("pagamentos.recusados")
                .build();
    }
    
    @Bean
    public Queue filaPagamentoParcial() {
        return QueueBuilder
                .nonDurable("pagamentos.parciais")
                .build();
    }
    
    @Bean
    public Queue filaPagamentoConcluido() {
        return QueueBuilder
                .nonDurable("pagamentos.finalizados")
                .build();
    }

//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new FanoutExchange("pagamentos.ex");
//    }
    
//    @Bean
//    public Binding juncaoFilas() {
//    	return BindingBuilder
//    			.bind("nomeFila")
//    			.to("paraQualExchange");
//    }

}