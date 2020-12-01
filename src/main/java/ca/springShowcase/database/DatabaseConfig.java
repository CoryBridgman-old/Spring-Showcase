package ca.springShowcase.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * A configuration class used to assist with jdbc database manipulation
 * @author Cory Bridgman
 * November 2020
 */
@Configuration
public class DatabaseConfig {

	/**
	 * Create new jdbc template for passing values from an object into the database
	 * @param dataSource
	 * @return
	 */
	@Bean 
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

}
