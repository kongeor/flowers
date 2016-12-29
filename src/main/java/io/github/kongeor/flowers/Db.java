package io.github.kongeor.flowers;

import io.github.kongeor.flowers.domain.Flower;
import io.github.kongeor.flowers.domain.User;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.mapper.ObjectMappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

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

	objectMappers = ObjectMappers.builder()
//	    .extractors(Collections.singletonMap(Flower.class, rs -> ))
	    .build();
	// rs -> new Flower(rs.getLong("ID"), rs.getString("NAME"), rs.getString("DESCRIPTION")
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
}
