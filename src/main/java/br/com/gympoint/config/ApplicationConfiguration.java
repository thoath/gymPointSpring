package br.com.gympoint.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracoes gerais da aplicacao
 * @author lucaskoch
 *
 */
@Configuration
public class ApplicationConfiguration {

	
   @Bean
   public ModelMapper modelMapper() {
     
      return new ModelMapper();
   }
	   
}
