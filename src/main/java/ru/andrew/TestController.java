package ru.andrew;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.andrew.exceptions.BadEncodingException;
import ru.andrew.exceptions.BadInputException;
import ru.andrew.exceptions.EmptyBodyException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(
        name = "Основной контроллер",
        description = "Содержит единственный метод для решения заданной задачи"
)
@Validated
public class TestController {

    private final TestService testService;

    @Operation(summary = "Подсчёт частоты встречаемости символов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "40001", description = "Отсуствует необходимое поле запроса"),
            @ApiResponse(responseCode = "40002", description = "Передана пустая строка"),
            @ApiResponse(responseCode = "40003", description = "Неверная кодировка текста"),
            @ApiResponse(responseCode = "40004", description = "Слишком длинный запрос"),
            @ApiResponse(responseCode = "415", description = "Неверный content-type")
    })
    @PostMapping(value = "/calcFrequency", produces = "application/json;charset=utf-8")
    private ResponseEntity<OutputDto> calculateFrequency(
            @RequestBody @Valid @Parameter(name = "Входные данные") InputDto inputDto
    ) throws BadInputException, EmptyBodyException, BadEncodingException {
        return ResponseEntity.ok(testService.calculateFrequency(inputDto));
    }
}
