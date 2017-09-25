package ua.nure.romanenko.st4.dbcp;


import org.apache.log4j.Logger;
import ua.nure.romanenko.st4.annotation.Column;
import ua.nure.romanenko.st4.annotation.Table;
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

    private static final Logger logger = Logger.getLogger(Query.class);

    public Dto readRow(Integer id, Class<? extends Dto> clazz) throws SQLException {
        String query = new QueryBuilder(clazz)
                .setWhere(String.format("%s = %d ", getIdName(clazz), id))
                .build();

        List<Dto> dtos = (List<Dto>) readDtos(query, clazz);
        if (dtos.size() != 0) return dtos.get(0);
        return null;
    }

    public Dto readRow(Dto filter) throws SQLException {
        Class clazz = filter.getClass();
        String query = new QueryBuilder(clazz)
                .setWhere(buildFilter(filter))
                .build();
        System.out.println("query : " + query);
        List<Dto> dtos = readDtos(query, clazz);
        if (dtos.size() != 0) return dtos.get(0);
        return null;
    }

    public List<?> readRows(Class<? extends Dto> clazz) throws SQLException {
        return readRows(clazz, null, null);
    }

    public List<?> readRows(Class<? extends Dto> clazz, Integer limit) throws SQLException {
        return readRows(clazz, null, limit);
    }

    public List<?> readRows(Class<? extends Dto> clazz, String from) throws SQLException {
        return readRows(clazz, from, null);
    }

    public List<?> readRows(Class<? extends Dto> clazz, String from, Integer limit) throws SQLException {
        String query = new QueryBuilder(clazz)
                .setFrom(from)
                .setLimit(limit)
                .build();

        return readDtos(query, clazz);
    }

    public List<?> readRows(Dto filter) throws SQLException {
        return readRows(filter, null);
    }

    public List<?> readRows(Dto filter, Integer limit) throws SQLException {
        Class clazz = filter.getClass();
        String query = new QueryBuilder(clazz)
                .setWhere(buildFilter(filter))
                .setLimit(limit)
                .build();

        return readDtos(query, clazz);
    }

    private List<?> readDtos(String query, Class<? extends Dto> clazz) throws SQLException {
        logger.info("sql : " + query);
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

    public QueryBuilder getQueryBuilder(Class<? extends Dto> dto) {
        return new QueryBuilder(dto);
    }

    public class QueryBuilder {

        private Class clazzFrom;

        private String select;
        private String from;
        private String where = "";
        private String limit = "";
        private String order = "";

        private StringBuilder join = new StringBuilder("");

        public QueryBuilder(Class<? extends Dto> from) {
            clazzFrom = from;
            setSelect(from);
            setFrom(from);
        }

        //// TODO: 25.09.17 test
        public QueryBuilder setSelect(Class<? extends Dto> clazz) {
            String prefix = getPrefix(clazz);
            select = String.format("SELECT %s ", getColumnsNames(clazz, prefix));
            return this;
        }

        private String getPrefix(Class<? extends Dto> clazz) {
            return clazz.getAnnotation(Table.class).value().substring(0, 1);
        }

        @Deprecated
        public QueryBuilder setSelect(String value) {
            if (value != null)
                this.select = String.format("SELECT %s ", value);
            return this;
        }

        public QueryBuilder setJoin(Class<? extends Dto> joinTable, Field joinIdField, Field fromIdField) {
            String tableName = joinTable.getAnnotation(Table.class).value();
            String joinPrefix = getPrefix(joinTable);
            String joinOn = joinIdField.getAnnotation(Column.class).value();
            String fromOn = fromIdField.getAnnotation(Column.class).value();

            join.append(String.format("JOIN %s %s ON %s.%s = %s.%s ",
                    tableName, joinPrefix,
                    joinPrefix, joinOn,
                    getPrefix(clazzFrom), fromOn));

            return this;
        }

        public QueryBuilder setFrom(Class<? extends Dto> clazz) {
            this.from = String.format("FROM %s %s ", getTable(clazz), getPrefix(clazz));
            return this;
        }

        public QueryBuilder setFrom(String value) {
            if (value != null)
                this.from = String.format("FROM %s %s ", value, getPrefix(clazzFrom));
            return this;
        }

        private static final String WHERE = "WHERE ";
        private static final String AND = " AND ";

        public QueryBuilder setWhere(Dto... dtos) {
            StringBuilder where = new StringBuilder("");

            for (Dto dto : dtos) {
                if (dto != null) {
                    if (where.length() == 0) where.append(WHERE);

                    String prefix = getPrefix(dto.getClass());
                    where.append(buildFilter(dto, prefix))
                            .append(AND);
                }
            }
            if (where.length() > WHERE.length())
                where.delete(where.length() - AND.length(), where.length());
            this.where = where.toString();
            return this;
        }

        public QueryBuilder setWhere(String value) {
            if (value != null)
                this.where = String.format("WHERE %s ", value);
            return this;
        }

        public QueryBuilder setLimit(Integer value) {
            if (value != null)
                this.limit = String.format("limit %d ", value);
            return this;
        }

        public QueryBuilder setOrder(String field) {
            if (field != null)
                this.order = String.format("order by %s ", field);
            return this;
        }

        public QueryBuilder setOrderDesc(String field) {
            if (field != null)
                this.order = String.format("order by %s desc ", field);
            return this;
        }

        public String build() {
            StringBuilder query = new StringBuilder();
            query.append(select)
                    .append(from)
                    .append(join)
                    .append(where)
                    .append(order)
                    .append(limit);
            return query.toString();
        }

        public List<?> readDtos() throws SQLException {
            String query = build();
            return Query.this.readDtos(query, clazzFrom);
        }
    }

}