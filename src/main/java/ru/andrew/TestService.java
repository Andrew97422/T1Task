package ru.andrew;

import org.springframework.stereotype.Service;
import ru.andrew.exceptions.BadEncodingException;
import ru.andrew.exceptions.BadInputException;
import ru.andrew.exceptions.EmptyBodyException;

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

            if (isBinaryData(str)) {
                throw new BadEncodingException("Нельзя передавать бинарный текст!");
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

    private static boolean isBinaryData(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (data.codePointAt(i) > 127 && data.codePointAt(i) < 1027 || data.codePointAt(i) > 1103) {
                return true;
            }
        }
        return false;
    }
}
