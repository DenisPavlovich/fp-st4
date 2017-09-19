package ua.nure.romanenko.st4.dbcp;


import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.dto.Dto;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by denis on 12.09.17.
 */
public class Query extends Component {

    public Dto readRow(Integer id, Class<? extends Dto> clazz) throws SQLException {
        String query = new QueryBuilder(clazz)
                .setWhere(String.format("%s = %d ", getIdName(clazz), id))
                .build();

        List<Dto> dtos = readDtos(query, clazz);
        if (dtos.size() != 0) return dtos.get(0);
        return null;
    }

    public Dto readRow(Dto filter) throws SQLException {
        Class clazz = filter.getClass();
        String query = new QueryBuilder(clazz)
                .setWhere(buildFilter(filter))
                .build();

        List<Dto> dtos = readDtos(query, clazz);
        if (dtos.size() != 0) return dtos.get(0);
        return null;
    }

    public List<Dto> readRows(Class<? extends Dto> clazz) throws SQLException {
        return readRows(clazz, null, null);
    }

    public List<Dto> readRows(Class<? extends Dto> clazz, Integer limit) throws SQLException {
        return readRows(clazz, null, limit);
    }

    public List<Dto> readRows(Class<? extends Dto> clazz, String from) throws SQLException {
        return readRows(clazz, from, null);
    }

    public List<Dto> readRows(Class<? extends Dto> clazz, String from, Integer limit) throws SQLException {
        String query = new QueryBuilder(clazz)
                .setFrom(from)
                .setLimit(limit)
                .build();

        return readDtos(query, clazz);
    }

    public List<Dto> readRows(Dto filter) throws SQLException {
        return readRows(filter, null);
    }

    public List<Dto> readRows(Dto filter, Integer limit) throws SQLException {
        Class clazz = filter.getClass();
        String query = new QueryBuilder(clazz)
                .setWhere(buildFilter(filter))
                .setLimit(limit)
                .build();

        return readDtos(query, clazz);
    }

    private List<Dto> readDtos(String query, Class<? extends Dto> clazz) throws SQLException {
        List<Dto> result = new LinkedList<>();
        Connection con = getConnection();
        try (Statement statement = con.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    result.add(buildDto(clazz, resultSet));
                }
            }
        } finally {
            con.close();
        }
        return result;
    }

    private Dto buildDto(Class<? extends Dto> clazz, ResultSet resultSet) throws SQLException {
        Dto dto = null;
        try {
            dto = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(dto, resultSet.getObject(field.getAnnotation(Column.class).value()));
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); //// TODO: 12.09.17 log
        }
        return dto;
    }

    public class QueryBuilder {
        private String select;
        private String from;
        private String where = "";
        private String limit = "";
        private String order = "";

        public QueryBuilder(Class<? extends Dto> clazz) {
            select = getColumnsNames(clazz);
            from = getTable(clazz);
        }

        public QueryBuilder setSelect(String value) {
            if (value != null)
                this.select = String.format("SELECT %s ", value);
            return this;
        }

        public QueryBuilder setFrom(String value) {
            if (value != null)
                this.from = String.format("FROM %s ", value);
            return this;
        }

        public QueryBuilder setWhere(String value) {
            if (value != null)
                this.where = String.format("WHERE %s ", value);
            return this;
        }

        public QueryBuilder setLimit(Integer value) {
            if (value != null)
                this.limit = String.format("limit %d", value);
            return this;
        }

        public QueryBuilder setOrder(String field) {
            if (field != null)
                this.order = String.format("order by %s ", field);
            return this;
        }

        public QueryBuilder setOrderDesc(String field) {
            if (field != null)
                this.order = String.format("order by %s desc", field);
            return this;
        }

        public String build() {
            StringBuilder query = new StringBuilder();
            query.append(select)
                    .append(from)
                    .append(where)
                    .append(order)
                    .append(limit);
            return query.toString();
        }
    }

}