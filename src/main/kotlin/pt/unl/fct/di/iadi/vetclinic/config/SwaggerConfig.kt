/**
Copyright 2019 João Costa Seco

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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
                    .title("Spring Boot REST API for IADI 2019/20")
                    .description("IADI 2019 VetClinic REST API")
                    .contact(Contact("Bernardo Amaral, Luís Grilo, João Veloso", "http://ctp.di.fct.unl.pt/~jcs", "joao.seco@fct.unl.pt"))
                    .license("Apache 2.0")
                    .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                    .version("1.0.0")
                    .build()

}