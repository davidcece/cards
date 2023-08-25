package com.cece.cards.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_VERSION = "1.0";
    private static final String API_TITLE = "Cards";
    private static final String API_DESCRIPTION = "Cards Management service";
    private static final String LICENSE_TEXT = "GNU General Public License";
    private static final String LICENSE_URL = "https://www.gnu.org/licenses/gpl-3.0.en.html";
    private static final String DEVELOPER_NAME = "David Cece";
    private static final String DEVELOPER_URL = "https://cece.com";
    private static final String DEVELOPER_EMAIL = "dvdcece@gmail.com";

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail(DEVELOPER_EMAIL);
        contact.setName(DEVELOPER_NAME);
        contact.setUrl(DEVELOPER_URL);

        License dctLicence = new License()
                .name(LICENSE_TEXT)
                .url(LICENSE_URL);

        Info info = new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .contact(contact)
                .description(API_DESCRIPTION)
                .termsOfService(DEVELOPER_URL)
                .license(dctLicence);

        return new OpenAPI().info(info);
    }

}

