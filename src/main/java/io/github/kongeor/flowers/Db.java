package io.github.kongeor.flowers;

import io.github.kongeor.flowers.domain.Flower;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;

import javax.sql.DataSource;

public class Db {

    private static DataSource dataSource;

    private static Query query;

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
	    .singleResult(rs -> new Flower(rs.getLong("ID"), rs.getString("NAME"), rs.getString("DESCRIPTION")));
    }
}
