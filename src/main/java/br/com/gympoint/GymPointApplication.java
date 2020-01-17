package br.com.gympoint;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Aplicacao de suporte ao dia a dia uma academia.
 *  - Cadastro de alunos.
 *  - Cadastro de planos mensais, trimestrais, etc.
 *  - Cadastro de um aluno para um plano.
 *  - Envio de emails no cadastro ou exclusao.
 *  - Pedido de ajuda de aluno para instrutor.
 *  - Navegacao autenticada pela api.
 * @author lucaskoch
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableMongoAuditing
public class GymPointApplication {

	@Value("${queue.order.name}")
    private String orderQueue;
	
	public static void main(String[] args) {
		SpringApplication.run(GymPointApplication.class, args);
	}
	
	/**
	 * Inicia o preceso de filas da rabbitMq
	 * @return
	 */
	@Bean
    public Queue queue() {
        return new Queue(orderQueue, true);
    }

}
