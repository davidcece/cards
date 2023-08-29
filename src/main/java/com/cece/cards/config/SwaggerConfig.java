package com.cece.cards.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.member1.username}")
    private String member1Username;
    @Value("${app.member1.password}")
    private String member1Password;

    @Value("${app.member2.username}")
    private String member2Username;
    @Value("${app.member2.password}")
    private String member2Password;

    private static final String API_VERSION = "1.0";
    private static final String API_TITLE = "Cards";
    private static final String LICENSE_TEXT = "GNU General Public License";
    private static final String LICENSE_URL = "https://www.gnu.org/licenses/gpl-3.0.en.html";
    private static final String DEVELOPER_NAME = "David Cece";
    private static final String DEVELOPER_URL = "https://cece.com";
    private static final String DEVELOPER_EMAIL = "dvdcece@gmail.com";
    public static final String SCHEME_NAME = "BearerScheme";
    public static final String SCHEME = "Bearer";

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail(DEVELOPER_EMAIL);
        contact.setName(DEVELOPER_NAME);
        contact.setUrl(DEVELOPER_URL);

        License license = new License()
                .name(LICENSE_TEXT)
                .url(LICENSE_URL);

        Info info = new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .contact(contact)
                .description(getDescription())
                .termsOfService(DEVELOPER_URL)
                .license(license);

        OpenAPI openApi = new OpenAPI().info(info);
        this.addSecurity(openApi);
        return openApi;
    }

    private String getDescription() {
        String lineBreak = "<br />";
        String password = ", password=";
        return "Cards Management service <br /> " +
                "Call /auth/login with below credentials to get a jwt token, " +
                "click Authorize button to set the token received and start manipulating cards <br />" +
                "1. Admin: username=" + adminUsername + password + adminPassword + lineBreak +
                "2. Member1: username=" + member1Username + password + member1Password + lineBreak +
                "3. Member2: username=" + member2Username + password + member2Password + lineBreak;
    }

    private void addSecurity(OpenAPI openApi) {
        Components components = this.createComponents();
        SecurityRequirement securityItem = new SecurityRequirement().addList(SCHEME_NAME);
        openApi.components(components).addSecurityItem(securityItem);
    }

    private Components createComponents() {
        var components = new Components();
        components.addSecuritySchemes(SCHEME_NAME, this.createSecurityScheme());
        return components;
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme().name(SCHEME_NAME).type(SecurityScheme.Type.HTTP).scheme(SCHEME);
    }

}

