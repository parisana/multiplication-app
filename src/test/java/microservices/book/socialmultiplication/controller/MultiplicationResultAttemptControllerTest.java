package microservices.book.socialmultiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.socialmultiplication.domain.Multiplication;
import microservices.book.socialmultiplication.domain.MultiplicationResultAttempt;
import microservices.book.socialmultiplication.domain.User;
import microservices.book.socialmultiplication.service.MultiplicationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Parisana
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {
    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    MockMvc mockMvc;

    private JacksonTester<MultiplicationResultAttempt> multiplicationResultAttemptJacksonTester;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @Before
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParameterizedTest(true);
    }

    @Test
    public void postResultReturnIncorrect() throws Exception {
        genericParameterizedTest(false);
    }

    @Test
    public void getUserStats() throws Exception {
        // Given user "john_doe" and his 2 submissions
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(20, 30);

        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 10, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 600, false);

        List<MultiplicationResultAttempt> submissionList = Lists.newArrayList(attempt1, attempt2);

        given(multiplicationService.getStatsForUser("john_doe")).willReturn(submissionList);

        // when the controller is hit
        final MockHttpServletResponse response = mockMvc
                .perform(get("/results")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("alias", "john_doe"))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(jsonResultAttemptList.write(submissionList).getJson());
    }

    void genericParameterizedTest(final boolean correct) throws Exception{
        // given
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(correct);

        User user = new User("John_doe");
        Multiplication multiplication = new Multiplication(20, 30);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 600, correct);

        // when
        MockHttpServletResponse mockHttpServletResponse =
                mockMvc
                        .perform(post("/results/").contentType(MediaType.APPLICATION_JSON).content(multiplicationResultAttemptJacksonTester.write(multiplicationResultAttempt).getJson()))
                .andReturn()
                .getResponse();

        // then
        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(mockHttpServletResponse.getContentAsString()).isEqualTo(multiplicationResultAttemptJacksonTester.write(new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(),
                                                                                                                                                            multiplicationResultAttempt.getMultiplication(),
                                                                                                                                                            multiplicationResultAttempt.getResultAttempt(),
                                                                                                                                                            correct)).getJson());
    }
}