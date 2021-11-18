package customers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/first_db");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table if exists customers");
        jdbcTemplate.execute("create table customers(" +
                "id serial, first_name varchar(15), last_name varchar(15))");

        String[] names = "John Woo;Jeff Dean;Josh Bloch;Josh Long".split(";");
        for (String fullname : names) {
            String[] name = fullname.split( " ");
            System.out.printf("Inserting customer record for %s %s\n", name[0], name[1]);
            jdbcTemplate.update("insert into customers(first_name, last_name) values (?, ?)",
                    name[0], name[1]);
        }

        System.out.println("Querying for customer records where first_name = 'Josh':");
        List<Customer> results = jdbcTemplate.query(
                "select * from customers where first_name = 'Josh'",
                (rs, rowNum) -> new Customer(rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")));

        for (Customer customer : results) {
            System.out.println(customer);
        }
    }
}
