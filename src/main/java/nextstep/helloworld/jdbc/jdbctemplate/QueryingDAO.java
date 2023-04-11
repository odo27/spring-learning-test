package nextstep.helloworld.jdbc.jdbctemplate;

import java.util.List;

import nextstep.helloworld.jdbc.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class QueryingDAO {
    private JdbcTemplate jdbcTemplate;

    public QueryingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Customer> actorRowMapper = (resultSet, rowNum) -> {
        Customer customer = new Customer(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
        return customer;
    };

    /**
     * public <T> T queryForObject(String sql, Class<T> requiredType)
     */
    public int count() {
        String sql = "select count(*) from customers";
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * public <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args)
     */
    public String getLastName(Long id) {
        String sql = "select last_name from customers where id = ?";
        return this.jdbcTemplate.queryForObject(
                sql,
                String.class, id);
    }

    /**
     * public <T> T queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args)
     */
    public Customer findCustomerById(Long id) {
        String sql = "select id, first_name, last_name from customers where id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> new Customer(id,
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")),
                id);
    }

    /**
     * public <T> List<T> query(String sql, RowMapper<T> rowMapper)
     */
    public List<Customer> findAllCustomers() {
        String sql = "select id, first_name, last_name from customers";
        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Customer customer = new Customer(
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"));
                    return customer;
                });
    }

    /**
     * public <T> List<T> query(String sql, RowMapper<T> rowMapper, @Nullable Object... args)
     */


    public List<Customer> findCustomerByFirstName(String firstName) {
        String sql = "select id, first_name, last_name from customers where first_name = ?";


        return this.jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Customer customer = new Customer(
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"));
                    return customer;
                }, firstName);
    }
}
