package br.com.gympoint.job;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.gympoint.dto.RegistrationDto;
import br.com.gympoint.service.EmailService;

/**
 * Listner de envio de email
 * Recebe mensagens rabbitMq para envio do email
 * @author lucaskoch
 *
 */
@Component
public class EmailSenderJob {

	@Autowired
	private EmailService emailService;
	
	/**
	 * Recebe uma mensagem de registro, cadastro ou cancelamento
	 * @param registration
	 */
	@RabbitListener(queues = {"${queue.order.name}"})
    public void receive(@Payload RegistrationDto registration) {
		
		if (registration.getCanceledAt() != null) {
			this.sendRegistrationCancelationMail(registration);
		} else {
			this.sendNewRegistrationMail(registration);
		}
		
    }
	
	/**
	 * Envia o email de novo cadastro
	 * @param registration
	 */
	private void sendNewRegistrationMail(RegistrationDto registration) {
		
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		StringBuffer stgB = new StringBuffer("olá Sr(a) ")
		.append(registration.getStudent().getName())
		.append(".Você foi registrado com sucesso no plano ")
		.append(registration.getContract().getTitle())
		.append(" pelo valor de ")
		.append(numberFormat.format(registration.getPrice()))
		.append(". Sua vigência é de ")
		.append(registration.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter))
		.append(" - ")
		.append(registration.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter));
		
		emailService.sendSimpleMessage(
				registration.getStudent().getEmail(), 
				"Equipe Gym Point <noreply@gympoint.com.br>", 
				"Nova Matrícula",
				stgB.toString());
	}
	
	/**
	 * Envia o email de cancelamento do contrato
	 * @param registration
	 */
	private void sendRegistrationCancelationMail(RegistrationDto registration) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		StringBuffer stgB = new StringBuffer("olá Sr(a) ")
		.append(registration.getStudent().getName())
		.append(". Seu plano ")
		.append(registration.getContract().getTitle())
		.append(" foi cancelado com sucesso")
		.append(" no dia ")
		.append(registration.getCanceledAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter))
		.append(". Agradecemos a sua participação.");
		
		emailService.sendSimpleMessage(
				registration.getStudent().getEmail(), 
				"Equipe Gym Point <noreply@gympoint.com.br>", 
				"Nova Matrícula",
				stgB.toString());
	}
	
}
