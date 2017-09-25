package ua.nure.romanenko.st4.dbcp;

import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Id;
import ua.nure.romanenko.st4.annotation.Table;
import ua.nure.romanenko.st4.dto.Dto;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by denis on 12.09.17.
 */
abstract class Component {

    protected SimpleDateFormat dateFormat;

    {
        dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    }

    protected String getColumnsValuesWithoutId(Class clazz, Dto dto) {
        StringBuilder values = new StringBuilder();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) == null) {
                field.setAccessible(true);
                try {
                    Object value = field.get(dto);
                    values.append(String.format("%s, ", convertToString(value)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); //// TODO: 11.09.17 log
                }
            }
        }
        values.delete(values.length() - 2, values.length());
        return values.toString();
    }

    protected String getColumnsNamesWithoutId(Class clazz) {
        StringBuilder names = new StringBuilder();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) == null)
                names.append(field.getAnnotation(Column.class).value())
                        .append(", ");
        }
        names.delete(names.length() - 2, names.length());

        return names.toString();
    }

    protected String getColumnsNames(Class clazz) {
        return getColumnsNames(clazz, "");
    }

    protected String getColumnsNames(Class clazz, String prefix) {
        if (!"".equals(prefix)) prefix = prefix.concat(".");

        StringBuilder names = new StringBuilder();

        for (Field field : clazz.getDeclaredFields()) {
            names.append(prefix + field.getAnnotation(Column.class).value())
                    .append(", ");
        }
        names.delete(names.length() - 2, names.length());

        return names.toString();
    }

    protected String getTable(Class clazz) {
        return ((Table) clazz.getAnnotation(Table.class)).value();
    }

    protected Long getIdValue(Class clazz, Dto dto) {
        for (Field field : clazz.getDeclaredFields()) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                field.setAccessible(true);
                try {
                    return ((Long) field.get(dto));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); //// TODO: 11.09.17 log
                }
            }
        }
        return null;
    }

    protected String getIdName(Class clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Id id = field.getAnnotation(Id.class);
            if (id != null && field.getAnnotation(Column.class) != null) {
                return field.getAnnotation(Column.class).value();
            }
        }
        return null;
    }

    protected Connection getConnection() throws SQLException {
        return ConnectionPool.getConnection();
    }

    protected String buildFilter(Dto dto) {
        return buildFilter(dto, "");
    }

    protected String buildFilter(Dto dto, String prefix) {
        if (!"".equals(prefix)) prefix = prefix.concat(".");
        StringBuilder filters = new StringBuilder();
        Class clazz = dto.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(dto);
                if (value != null)
                    filters.append(String.format("%s%s = %s AND ", prefix,
                            field.getAnnotation(Column.class).value(),
                            convertToString(value)));
            } catch (IllegalAccessException e) {
                e.printStackTrace(); //// TODO: 15.09.17 log
            }
        }
        if (filters.length() > 5)
            filters.delete(filters.length() - 5, filters.length());
        return filters.toString();
    }

    protected String convertToString(Object value) {
        if (value == null) return null;

        if (value instanceof Date)
            return String.format("\'%s\'", dateFormat.format(value));
        else if (!(value instanceof Integer)
                && !(value instanceof Double)
                && !(value instanceof Long))
            return String.format("\'%s\'", value);
        return value.toString();
    }
}
