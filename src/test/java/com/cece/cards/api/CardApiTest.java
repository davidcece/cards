package com.cece.cards.api;

import com.cece.cards.dto.requests.AuthRequest;
import com.cece.cards.dto.requests.CardRequest;
import com.cece.cards.dto.requests.UpdateCardRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = {"classpath:application-test.properties"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CardApiTest {

    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    void shouldDenyUnauthorizedAccess() throws Exception {
        mockMvc.perform(post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        AuthRequest request = AuthRequest.builder()
                .email(adminUsername)
                .password("dfsfsdfsfsdfsfsfsfsfsfsfs")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenValidCredentialsShouldReturnNonEmptyToken() throws Exception {
        AuthRequest request = AuthRequest.builder()
                .email(adminUsername)
                .password(adminPassword)
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }


    @Test
    @Order(1)
    @WithMockUser("member1@cards.com")
    void member1CanCreateCard() throws Exception {
        mockMvc.perform(post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardRequestValid())))
                .andExpect(status().isOk());

        mockMvc.perform(post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardRequestOptionalDescAndColor())))
                .andExpect(status().isOk());

        mockMvc.perform(post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardRequestInvalidColor())))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardRequestEmptyName())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    @WithMockUser("member2@cards.com")
    void member2CanCreateCard() throws Exception {
        mockMvc.perform(post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardRequestValid())))
                .andExpect(status().isOk());

        mockMvc.perform(post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cardRequestOptionalDescAndColor())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithMockUser("member1@cards.com")
    void shouldHaveAccessToTheirCards() throws Exception {
        mockMvc.perform(get("/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)));
        mockMvc.perform(get("/v1/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(3)
    @WithMockUser("member1@cards.com")
    void shouldNotHaveAccessToOtherMemberCards() throws Exception {
        mockMvc.perform(get("/v1/cards/3"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    @WithMockUser("admin@cards.com")
    void adminShouldHaveAccessToAllCards() throws Exception {
        mockMvc.perform(get("/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(4)));
    }

    @Test
    @Order(4)
    @WithMockUser("member1@cards.com")
    void memberCanModifyCard() throws Exception {
        mockMvc.perform(put("/v1/cards/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateCardRequestValid())))
                .andExpect(status().isOk());

        mockMvc.perform(put("/v1/cards/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateCardRequestInvalidStatus())))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put("/v1/cards/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateCardRequestClearColorAndDescription())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").isEmpty())
                .andExpect(jsonPath("$.description").isEmpty());

        mockMvc.perform(delete("/v1/cards/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithMockUser("admin@cards.com")
    void shouldNotHaveCardsCreatedInTheFuture() throws Exception {
        LocalDate now = LocalDate.now();
        String fromDate = now.plusDays(1).format(formatter);
        String toDate = now.plusDays(2).format(formatter);

        String url = String.format("/v1/cards/search?fromDate=%s&toDate=%s", fromDate, toDate);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @Order(5)
    @WithMockUser("admin@cards.com")
    void shouldHaveCardsCreatedInThePast24Hours() throws Exception {
        LocalDate now = LocalDate.now();
        String fromDate = now.plusDays(-1).format(formatter);
        String toDate = now.format(formatter);

        String url = String.format("/v1/cards/search?fromDate=%s&toDate=%s", fromDate, toDate);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(3)));
    }

    @Test
    @Order(5)
    @WithMockUser("admin@cards.com")
    void shouldHave2CardsWithAwesomeName() throws Exception {
        String url = "/v1/cards/search?name=Awesome Name";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    @Order(5)
    @WithMockUser("admin@cards.com")
    void invalidSortByShouldReturnBadRequest() throws Exception {
        String sortBy = "unknownColumn";
        String url = String.format("/v1/cards/search?sortBy=%s&sortDescending=%s", sortBy, true);

        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    @WithMockUser("admin@cards.com")
    void shouldHaveCardsSortedByDateDesc() throws Exception {
        String sortBy = "createdAt";
        String url = String.format("/v1/cards/search?sortBy=%s&sortDescending=%s", sortBy, true);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(4));
    }

    @Test
    @Order(5)
    @WithMockUser("member2@cards.com")
    void memberShouldHaveCardsSortedByDateDesc() throws Exception {
        String sortBy = "createdAt";
        String url = String.format("/v1/cards/search?sortBy=%s&sortDescending=%s", sortBy, true);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(4));
    }

    @Test
    @Order(5)
    @WithMockUser("member2@cards.com")
    void memberShouldSearchByStatusAndColor() throws Exception {
        String status = "To Do";
        String color = encodeURL("#ff1244");
        String url = String.format("/v1/cards/search?status=%s&color=%s", status, color);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    @Order(5)
    @WithMockUser("admin@cards.com")
    void canLimitResults() throws Exception {
        int page = 1;
        int size = 2;

        String url = String.format("/v1/cards/search?page=%d&pageSize=%d", page, size);
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    private String encodeURL(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    CardRequest cardRequestValid() {
        return CardRequest.builder()
                .name("Awesome Name")
                .description("Some desc")
                .color("#ff1244")
                .build();
    }

    CardRequest cardRequestOptionalDescAndColor() {
        return CardRequest.builder()
                .name("Given Name")
                .description(null)
                .color(null)
                .build();
    }

    CardRequest cardRequestInvalidColor() {
        return CardRequest.builder()
                .name("Card 1")
                .description("Some desc")
                .color("24")
                .build();
    }

    CardRequest cardRequestEmptyName() {
        return CardRequest.builder()
                .name("")
                .description("Some desc")
                .color("#fff")
                .build();
    }

    UpdateCardRequest updateCardRequestValid() {
        return UpdateCardRequest.builder()
                .name("Given Name")
                .description("Done working on it")
                .status("Done")
                .color("#aaa")
                .build();
    }

    UpdateCardRequest updateCardRequestInvalidStatus() {
        return UpdateCardRequest.builder()
                .name("Given Name")
                .description("Done working on it")
                .status("Is Done")
                .color("#aaa")
                .build();
    }

    UpdateCardRequest updateCardRequestClearColorAndDescription() {
        return UpdateCardRequest.builder()
                .name("Given Name")
                .description(null)
                .status("Done")
                .color(null)
                .build();
    }


}