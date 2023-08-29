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

    @Value("${app.user1.username}")
    private String user1Username;
    @Value("${app.user1.password}")
    private String user1Password;

    @Value("${app.user2.username}")
    private String user2Username;
    @Value("${app.user2.password}")
    private String user2Password;

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
        return  "Cards Management service <br /> " +
                "Call /auth/login with below credentials to get a jwt token, " +
                "click Authorize button to set the token received and start manipulating cards <br />" +
                "1. Admin: username=" + adminUsername + ", password=" + adminPassword + "<br />" +
                "2. User1: username=" + user1Username + ", password=" + user1Password + "<br />" +
                "3. User2: username=" + user2Username + ", password=" + user2Password + "<br />";
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

