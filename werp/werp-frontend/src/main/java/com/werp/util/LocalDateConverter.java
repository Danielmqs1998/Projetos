package com.werp.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(forClass = LocalDate.class, value = "localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate getAsObject(
            FacesContext context,
            UIComponent component,
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value, FORMATTER);
    }

    @Override
    public String getAsString(
            FacesContext context,
            UIComponent component,
            LocalDate value) {

        if (value == null) {
            return "";
        }
        return value.format(FORMATTER);
    }
}