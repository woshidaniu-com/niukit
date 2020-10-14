package org.springframework.enhanced.http.converter.json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by yuananyun on 2015/11/23.
 */
public class MappingJacksonHttpMessageConverterEx extends MappingJackson2HttpMessageConverter {
    private ObjectMapper objectMapper = new ObjectMapper();

    public MappingJacksonHttpMessageConverterEx() {
        super();
        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig()
                .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setConfig(deserializationConfig);
        // Configure serialization
        SerializationConfig serializationConfig = objectMapper.getSerializationConfig()
                .without(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        //serializationConfig.withDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setConfig(serializationConfig);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,true);

        setObjectMapper(objectMapper);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        JavaType javaType = getJavaType(null, clazz);
        return this.objectMapper.readValue(inputMessage.getBody(), javaType);
    }
}