package br.com.microservico.operacoes.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.microservico.operacoes.dto.PagamentoDto;

@Component
public class PagamentoListener {

    //@RabbitListener(queues = "pagamentos.operacao")
    public void recebeMensagem(@Payload PagamentoDto pagamento) {
        String mensagem = """
                Número da operação: %s
                Valor R$: %s
                """.formatted(pagamento.getOpeCodigo(),
                pagamento.getValor());

        System.out.println("Recebi a mensagem " + mensagem);
    }
}
