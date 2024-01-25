package ru.andrew;

import org.springframework.stereotype.Service;
import ru.andrew.exceptions.BadEncodingException;
import ru.andrew.exceptions.BadInputException;
import ru.andrew.exceptions.EmptyBodyException;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestService {

    public OutputDto calculateFrequency(InputDto inputDto) throws BadInputException, EmptyBodyException, BadEncodingException {
        if (inputDto.getData() == null)  throw new EmptyBodyException("Отсуствует необходимое поле запроса!");
        if (inputDto.getData().isEmpty())   throw new BadInputException("Пустая строка недопустима!");
        else {
            String str = inputDto.getData();    //њз9Оsњз9М
            if (str.getBytes(StandardCharsets.UTF_8).length != str.length()) {
                throw new BadEncodingException("Кодировка текста должна быть в формате UTF-8!");
            }
            Map<String, Integer> result = calculate(str);
            return OutputDto.builder().data(sortMap(result)).build();
        }
    }

    private Map<String, Integer> calculate(String str) {
        Map<String, Integer> result = new HashMap<>();

        for (int i = 0; i < str.length(); i++) {
            if (result.containsKey(String.valueOf(str.charAt(i)))) {
                int value = result.get(String.valueOf(str.charAt(i)));
                value += 1;
                result.put(String.valueOf(str.charAt(i)), value);
            } else {
                result.put(String.valueOf(str.charAt(i)), 1);
            }
        }

        return result;
    }

    private Map<String, Integer> sortMap(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {throw new AssertionError();},
                        LinkedHashMap::new
                ));
    }
}
