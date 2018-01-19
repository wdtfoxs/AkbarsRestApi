package ru.akbarsdigital.restapi.configurations.root;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ru.akbarsdigital.restapi.configurations.root.db.DataSourceConfig;
import ru.akbarsdigital.restapi.configurations.root.security.SecurityConfig;

@Configuration
@Import({SecurityConfig.class, DataSourceConfig.class})
@EnableAspectJAutoProxy
@PropertySources(
        {@PropertySource("classpath:spring/application.properties"),
                @PropertySource("classpath:spring/${spring.active.profiles:dev}/db.properties")}
)
@ComponentScan(value = "ru.akbarsdigital.restapi", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru\\.itbrick\\.trastonline\\.((configurations)|(web))\\..*"))
public class ApplicationConfig {

    @Bean
    public MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
