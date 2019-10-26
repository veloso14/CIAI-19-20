package pt.unl.fct.di.iadi.vetclinic.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    @Bean
    fun api(): Docket =
            Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("pt.unl.fct.di.iadi.vetclinic"))
                    .paths(PathSelectors.any())
                    .build().apiInfo(apiEndPointsInfo());

    fun apiEndPointsInfo(): ApiInfo =
            ApiInfoBuilder()
                    .title("Spring Boot REST API Example for IADI 2019/20")
                    .description("IADI 2019 VetClinic REST API")
                    .contact(Contact("João Costa Seco, Eduardo Geraldo", "http://ctp.di.fct.unl.pt/~jcs", "joao.seco@fct.unl.pt"))
                    .license("Apache 2.0")
                    .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                    .version("1.0.0")
                    .build()

}