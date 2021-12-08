package ru.oz.demostatemachine.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * LockConverter.
 *
 * @author Igor_Ozol
 */
//@Slf4j
//@Converter
//public class LockConverter implements AttributeConverter<Lock<?>, String> {
//
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    {
//        mapper.registerModule(new JavaTimeModule());
//    }
//
//    @Override
//    public String convertToDatabaseColumn(Lock<?> lock) {
//        return Try.of(()-> mapper.writeValueAsString(lock))
//                .onFailure(error -> log.error("error while convertToDatabaseColumn() {} ", lock, error))
//                .getOrElse("error");
//    }
//
//    @Override
//    public Lock<?> convertToEntityAttribute(String s) {
//        return Try.of(()-> mapper.readValue(s, Lock.class))
//                .onFailure(error -> log.error("error while convertToEntityAttribute() {} ", s, error))
//                .getOrNull();
//    }
//}
