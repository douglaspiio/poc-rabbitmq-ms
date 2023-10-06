package br.com.microservico.pagamentos.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.microservico.pagamentos.dto.OperacaoResponse;

@Component
public class OperacaoListener {
	//@RabbitListener(queues = "operacao.realizada")
    public void recebeMensagem(@Payload OperacaoResponse operacao) {
        String mensagem = """ 
                Codigo da operacao: %s
                Quantidade de titulos: %s
                Valor total: %s
                Status: %s 
                """.formatted(operacao.getCodigoOperacao(), 
                		operacao.getQuantidadeTitulos(), 
                		operacao.getValorOperacao(), 
                		operacao.getStatusOperacao());

        System.out.println("Recebi a mensagem \n" + mensagem);
    }

}
