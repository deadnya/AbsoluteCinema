package com.absolute.cinema.mapper;

import com.absolute.cinema.dto.SessionDTO;
import com.absolute.cinema.dto.TimeslotDTO;
import com.absolute.cinema.entity.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Date;
import java.time.OffsetDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SessionMapper {
    @Mapping(target = "filmId", source = "film.id")
    @Mapping(target = "hallId", source = "hall.id")
    @Mapping(target = "startAt", source = "startAt", qualifiedByName = "offsetDateTimeToDate")
    @Mapping(target = "timeslot", source = "session", qualifiedByName = "createTimeslot")
    SessionDTO toDTO(Session session);

    @Named("offsetDateTimeToDate")
    default Date map(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? Date.from(offsetDateTime.toInstant()) : null;
    }

    @Named("createTimeslot")
    default TimeslotDTO createTimeslot(Session session) {
        if (session.getStartAt() == null) {
            return null;
        }
        Date start = Date.from(session.getStartAt().toInstant());
        Date end = Date.from(session.getStartAt().plusMinutes(session.getFilm().getDurationMinutes()).toInstant());
        return new TimeslotDTO(start, end);
    }
}