package ru.andrew;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@Schema(name = "Выходные данные")
public class OutputDto {

    @Schema(name = "Данные по частоте встречаемости символов в строке")
    private Map<String, Integer> data;
}
