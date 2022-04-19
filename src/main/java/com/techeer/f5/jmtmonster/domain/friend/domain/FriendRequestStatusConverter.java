package com.techeer.f5.jmtmonster.domain.friend.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FriendRequestStatusConverter implements
        AttributeConverter<FriendRequestStatus, String> {

    @Override
    public String convertToDatabaseColumn(FriendRequestStatus attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.name();
    }

    @Override
    public FriendRequestStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        return FriendRequestStatus.valueOf(dbData);
    }
}
