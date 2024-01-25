package ru.andrew;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(name = "DTO на вход в программу")
@Data
public class InputDto {

    @Schema(name = "data", description = "Строка для подсчёта", minimum = "1", maximum = "20000000")
    @Length(max = 20000000)
    private String data;
}
