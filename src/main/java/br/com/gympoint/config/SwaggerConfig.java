 package br.com.gympoint.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuracoes do swagger, ferramenta de documentacao da api
 * @author lucaskoch
 * @see https://swagger.io/docs/specification/about/
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	   
	/**
	 * Configuracao do docket
	 *  - Define o pacote a ser documentado
	 *  - Adiciona a o preenchimento do token no header de cada requesicao
	 * @return
	 */
	@Bean
    public Docket api() {

		ParameterBuilder builder = new ParameterBuilder()
				.name("Authorization")
				.modelRef(new ModelRef("string"))
				.parameterType("header")
				.defaultValue("Bearer ")
				.required(true);
		
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(builder.build());
		
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("br.com.gympoint"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameters);
    }	
}
