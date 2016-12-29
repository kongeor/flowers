package io.github.kongeor.flowers;

import io.github.kongeor.flowers.domain.Flower;
import io.github.kongeor.flowers.domain.User;
import io.github.kongeor.flowers.domain.UserFlower;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.mapper.ObjectMapperRsExtractor;
import org.codejargon.fluentjdbc.api.mapper.ObjectMappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;

import javax.sql.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Db {

    private static DataSource dataSource;

    private static Query query;

    private static ObjectMappers objectMappers;

    static {
	PGPoolingDataSource source = new PGPoolingDataSource();
	source.setDataSourceName("Flowers Data Source");
	source.setServerName("localhost");
	source.setDatabaseName("flowers");
	source.setPortNumber(5432);
	source.setUser("flowers");
	source.setPassword("flowers");
	source.setMaxConnections(10);

	dataSource = source;

	FluentJdbc fluentJdbc = new FluentJdbcBuilder()
	    .connectionProvider(dataSource)
	    .build();

	query = fluentJdbc.query();

	Map<Class, ObjectMapperRsExtractor> extractors = new HashMap<>();

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// TODO works only for dates
	extractors.put(List.class, (resultSet, index) -> {
	    String[] tokens = resultSet.getString(index).replaceAll("\"|\\{|}", "").split(",");
	    List<Date> dates = new ArrayList<>();
	    for (String token : tokens) {
	        String date = token.split("\\.")[0]; // ignore millis
		try {
		    dates.add(simpleDateFormat.parse(date));
		} catch (ParseException e) {
		    throw new IllegalStateException(e);
		}
	    }
	    return dates;
	});

	objectMappers = ObjectMappers.builder()
	    .extractors(extractors)
	    .build();
    }

    public static void migrate() {
	Flyway flyway = new Flyway();
	flyway.setDataSource(dataSource);
	flyway.migrate();
    }

    public static void insertUser(String username, String password, String email) {
        query
	    .update("insert into users (username, password, email) values(?, ?, ?)")
	    .params(username, password, email)
	    .run();
    }

    public static Flower insertFlower(Flower flower) {
	return query
	    .select("insert into flowers (name, description) values(?, ?) returning *")
	    .params(flower.getName(), flower.getDescription())
	    .singleResult(objectMappers.forClass(Flower.class));
    }

    public static List<Flower> findAllFlowers() {
	return query
	    .select("select * from flowers")
	    .listResult(objectMappers.forClass(Flower.class));
    }

    public static Optional<User> findUserByNameOrEmail(String value) {
	return query
	    .select("select * from users where username = ? or email = ?")
	    .params(value, value)
	    .firstResult(objectMappers.forClass(User.class));
    }

    public static Optional<User> findUserById(Long id) {
	return query
	    .select("select * from users where id = ?")
	    .params(id)
	    .firstResult(objectMappers.forClass(User.class));
    }

    public static List<UserFlower> findUserFlowers(Long userId) {
	return query
	    .select("select * from user_flowers where user_id = ?")
	    .params(userId)
	    .listResult(objectMappers.forClass(UserFlower.class));
    }

    public static void waterUserFlower(Long id) {
	query
	    .update("update user_flowers set waterings = waterings || now()::timestamp where id = ?")
	    .params(id)
	    .run();
    }
}
