package ru.andrew;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest {

    private final String PATH = "/api/v1/calcFrequency";

    @Autowired
    private MockMvc mockMvc;


    @DisplayName("Проверка на обычный ввод")
    @Test
    void normalInputTest() throws Exception {
        String request = formRequest("AAAAbbbCCd");
        String response = """
                {"data":{"A":4,"b":3,"C":2,"d":1}}""";

        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(request)
        ).andExpect(status().isOk())
                .andExpect(result -> {
                    String resultString = result.getResponse().getContentAsString();
                    Assert.assertEquals(response, resultString);
                })
                .andDo(print())
                .andReturn();

    }

    @DisplayName("Передача пустой строки")
    @Test
    void emptyEnterTest() throws Exception {
        String request = formRequest("");
        String requiredPart = "Пустая строка недопустима!";
        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(request)
        ).andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String resultString = result.getResponse().getContentAsString();
                    assertThat(resultString, resultString.contains(requiredPart));
                })
                .andDo(print())
                .andReturn();

    }

    record FailInputDto(String dataFail) {}

    @DisplayName("Отсутствие необходимых полей запроса")
    @Test
    void emptyFieldEnterTest() throws Exception {
        FailInputDto inputTestDto = new FailInputDto("AAAAbbbCCd");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String request = writer.writeValueAsString(inputTestDto);
        String requiredPart = "Отсуствует необходимое поле запроса!";
        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(request)
        ).andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String resultString = result.getResponse().getContentAsString();
                    assertThat(resultString, resultString.contains(requiredPart));
                })
                .andDo(print())
                .andReturn();
    }

    @DisplayName("Неверная кодировка переданного текста")
    @Test
    void failEncodingEnterTest() throws Exception {
        String request = formRequest("њз9Оsњз9М");
        String requiredPart = "Кодировка текста должна быть в формате UTF-8!";
        this.mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(request)
                ).andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String resultString = result.getResponse().getContentAsString();
                    assertThat(resultString, resultString.contains(requiredPart));
                })
                .andDo(print())
                .andReturn();

    }

    @DisplayName("Неверный content-type")
    @Test
    void failContentTypeTest() throws Exception {
        String request = formRequest("AAAAbbbCCd");
        String requiredPart = "Неверный Content-Type, должен быть application/json";
        this.mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_PDF_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(request)
                ).andExpect(status().isUnsupportedMediaType())
                .andExpect(result -> {
                    String resultString = result.getResponse().getContentAsString();
                    assertThat(resultString, resultString.contains(requiredPart));
                })
                .andDo(print())
                .andReturn();
    }

    @DisplayName("Передана слишком длинная строка")
    @Test
    void tooLongRequest() throws Exception {
        String request = formRequest(createBigRequest());
        String requiredPart = "Ваш запрос слишком длинный, максимальная длина запроса - 20000000 символов";

        this.mockMvc.perform(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(request)
                ).andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String resultString = result.getResponse().getContentAsString();
                    assertThat(resultString, resultString.contains(requiredPart));
                })
                .andReturn();
    }

    private String formRequest(String str) throws JsonProcessingException {
        InputTestDto inputTestDto = new InputTestDto();
        inputTestDto.setData(str);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(inputTestDto);
    }

    private String createBigRequest() {
        StringBuilder builder = new StringBuilder("A");
        builder.append("A".repeat(20000001));
        return builder.toString();
    }
}