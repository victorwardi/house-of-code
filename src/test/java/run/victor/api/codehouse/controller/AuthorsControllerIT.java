package run.victor.api.codehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import run.victor.api.codehouse.repository.AuthorRepository;
import run.victor.api.codehouse.request.NewAuthorRequest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Victor Wardi - @victorwardi
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("/data/authors.sql")
@Sql(scripts = "/data/clean-authors.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthorsControllerIT {

    private static final String URL = "/v1/authors";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    AuthorRepository authorRepository;

    @BeforeAll

    static void beforeAll() {
        System.out.printf("###############################################");
    }

    @Test
    void whenAuthorValid_thenReturnsStatus200() throws Exception {
        NewAuthorRequest validAuthor = NewAuthorRequest.create("Bart Simpson", "bart@springfield.fox", "Random description");
        String requestBody = objectMapper.writeValueAsString(validAuthor);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody)).andExpect(status().isOk());
    }

    @Test
    void whenAuthorValid3_thenReturnsStatus200() throws Exception {
        NewAuthorRequest validAuthor = NewAuthorRequest.create("Bart Simpson", "bart@springfield.fox", "Random description");
        String requestBody = objectMapper.writeValueAsString(validAuthor);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody)).andExpect(status().isOk());
    }

    @Test
    void whenAuthorEmailAlreadyOnDataBase_thenReturnsStatus400() throws Exception {
        //An author with the same email "alberto@souza.com" is already on the database. An error will be thrown.
        NewAuthorRequest repeatedAuthorEmail = NewAuthorRequest.create("Bart Simpson", "alberto@souza.com", "Random description");
        String requestBody = objectMapper.writeValueAsString(repeatedAuthorEmail);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    void whenAuthorNameNotInformed_thenReturnsStatus400() throws Exception {
        NewAuthorRequest authorWithoutName = NewAuthorRequest.create(null, "bart@springfield.fox", "Random description");
        String requestBody = objectMapper.writeValueAsString(authorWithoutName);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Name must be informed")));
    }

    @Test
    void whenAuthorEmailNotInformed_thenReturnsStatus400() throws Exception {
        NewAuthorRequest authorWithoutEmail = NewAuthorRequest.create("Bart Simpson", null, "Random description");
        String requestBody = objectMapper.writeValueAsString(authorWithoutEmail);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Email must be informed")));
    }

    @Test
    void whenAuthorEmailInvalidFormat_thenReturnsStatus400() throws Exception {
        NewAuthorRequest authorWithoutValidEmail = NewAuthorRequest.create("Bart Simpson", "emailmail.com", "Random description");
        String requestBody = objectMapper.writeValueAsString(authorWithoutValidEmail);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("must be a well-formed email address")));
    }

    @Test
    void whenAuthorDescriptionNotInformed_thenReturnsStatus400() throws Exception {
        NewAuthorRequest authorWithoutDescription = NewAuthorRequest.create("Bart Simpson", "bart@springfield.fox", null);
        String requestBody = objectMapper.writeValueAsString(authorWithoutDescription);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Description must be informed")));
    }


    @Test
    void whenDescriptionBiggerThen400_thenReturnsStatus400() throws Exception {
        NewAuthorRequest authorWithVeryLongDescription = NewAuthorRequest.create("Bart Simpson", "bart@springfield.fox", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " + "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt " + "in culpa qui officia deserunt mollit anim id est laborum.");
        String requestBody = objectMapper.writeValueAsString(authorWithVeryLongDescription);
        mockMvc.perform(post(URL).contentType("application/json").content(requestBody))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("size must be between 0 and 400")));
    }
}
