package com.example.bankcards.entity.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Converter(autoApply = false)
@Component
public class CardNumberAttributeConverter implements AttributeConverter<String, String> {

    private static AesGcmEncryptor encryptor;

    @Autowired
    public void setEncryptor(AesGcmEncryptor enc) {
        encryptor = enc;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : encryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : encryptor.decrypt(dbData);
    }
}
