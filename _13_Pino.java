import java.sql.*;             
import java.util.*;             
import java.util.function.*;    
import java.util.stream.*;      
import java.text.DecimalFormat;

// Use JDBC connection to the access employee database. Retrieve the id, name and salary information to create Employee objects using the Employee class:
class Employee {
    private int id;
    private String name;
    private double salary;

    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', salary=%.2f}", id, name, salary);
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getSalary() {
        return salary;
    }
    // Setter for salary (to apply tax reductions)
    public void setSalary(double salary) {
        this.salary = salary;
    }
}

public class _13_Pino {
    public static void main(String[] args) {
        List<Employee> employeesList = new ArrayList<>();

    // Q1. Populate the list: List<Employee> employeesList = new ArrayList<Employee>();
        String url = "jdbc:mariadb://localhost:3306/employee"; // Assuming 'employee' is your database name
        String user = "pino";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, salary FROM employees")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double salary = rs.getDouble("salary");
                employeesList.add(new Employee(id, name, salary));
            }
            System.out.println("__Q1 + Q2__");
    // Q2. Print a list of all Employees
            System.out.println("Total Employees: " + employeesList.size()); //employee count
            System.out.println("List of all Employees:");  
            employeesList.forEach(System.out::println);            //employee list
            System.out.println();
        } catch (SQLException e) {
            System.err.println("Database connection or query failed: " + e.getMessage());
            e.printStackTrace();
            return; // Exit if database connection fails
        }

    // Q3. Define a Predicate to filter employees with salary > $50000. Apply this Predicate to filter the List of
    //    Employees to get highEarners<Employee> who earn more than $50000.
        Predicate<Employee> isHighEarner = employee -> employee.getSalary() > 50000;
        List<Employee> highEarners = employeesList.stream()
                                                .filter(isHighEarner)
                                                .collect(Collectors.toList());

        System.out.println("__Q3+Q4__");
    // Q4. Print a list of highEarners
        System.out.println("Total High Earners: " + highEarners.size());  //highEarners count
        System.out.println("List of highEarners (salary > $50,000):");
        highEarners.forEach(System.out::println);                 //highEarners list
        System.out.println();

    // Q5. Write a Function<Employee, Employee> named applyTax to apply a 15% tax reduction to highEarners. 
    //    The Function should take an Employee and return an Employee with adjusted salary.
        Function<Employee, Employee> applyTax = employee -> {
            // Create a new Employee object to avoid modifying the original list directly for this step,
            // or directly set the salary if modification of the original object is intended.
            // For streams, it's often better to create new objects for transformation.
            Employee taxedEmployee = new Employee(employee.getId(), employee.getName(), employee.getSalary() * 0.85);
            return taxedEmployee;
        };

    // Q6. Create a Function<Employee, String> named formatSalary to format the salary to 2 decimal places,
    //    with $ attached in the front, for example: $52000.30
        Function<Employee, String> formatSalary = employee -> {
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            return df.format(employee.getSalary());
        };

        System.out.println("__Q5,Q6,Q7__");
        List<Employee> highEarnersWithTaxedSalaries = employeesList.stream()
                                                            .filter(isHighEarner) // Filter for highEarners
                                                            .map(applyTax)        // Apply 15% tax
                                                            .collect(Collectors.toList());
        System.out.println("Total Employees Processed for Tax: " + highEarnersWithTaxedSalaries.size()); //count highEarners
        System.out.println("High Earners with 15% Taxed Salaries:");
        highEarnersWithTaxedSalaries.forEach(emp ->
            System.out.println(String.format("Employee{id=%d, name='%s', salary=%s}",
                                            emp.getId(), emp.getName(), formatSalary.apply(emp)))
        );
        System.out.println();

    // Q8. Use a single sequence of stream functions to take the list of employees, 
    //    apply a 10% tax to those earning less than or equal to $50,000, 15% to those earning above $50000, 
    //    and neatly format the Employee string to show their names and salaries ?
    //    (partitionBy or groupBy Collectors)
        System.out.println("__Q8:Employees with tiered tax and formatted salaries__");

        // Predicate for partitioning
        Predicate<Employee> isAbove50K = employee -> employee.getSalary() > 50000;

        // Function for 15% tax ( > $50,000)
        Function<Employee, Employee> apply15PercentTax = employee ->
                new Employee(employee.getId(), employee.getName(), employee.getSalary() * 0.85);

        // Function: 10% tax ( <= $50,000)
        Function<Employee, Employee> apply10PercentTax = employee ->
                new Employee(employee.getId(), employee.getName(), employee.getSalary() * 0.90);

        // Use partitioningBy and mapping to apply different taxes
        employeesList.stream()
                .collect(Collectors.partitioningBy(isAbove50K,
                         Collectors.mapping(
                             employee -> isAbove50K.test(employee) ? apply15PercentTax.apply(employee) : apply10PercentTax.apply(employee),
                             Collectors.toList()
                         )))
                .values().stream()        // Get both lists (true and false for isAbove50K)
                .flatMap(List::stream)    // Flatten into a single stream
                .sorted((e1, e2) -> Integer.compare(e1.getId(), e2.getId()))   // Sort by ID for consistent output
                .forEach(emp -> System.out.println(String.format("Employee: %s | Salary: %s",
                                                                 emp.getName(), formatSalary.apply(emp))));
    }
}