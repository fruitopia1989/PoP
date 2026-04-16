import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class _12_Pino {
    public static void main(String[] args) {
        // Check for command line arguments: [username] [password] [database]
        if (args.length < 3) {
            System.out.println("Usage: java Week12Exercises <user> <pass> <db>");
            return;
        }

        String user = args[0];
        String pass = args[1];
        String dbName = args[2];
        String url = "jdbc:mariadb://localhost:3306/" + dbName;

        List<SalesPerson> salesPersonList = new ArrayList<>();

        // SQL Query to get  data for SalesPerson objects, join salesman + orders to calculate the total sales per person
        String sql = "SELECT s.name, s.city, s.commission, SUM(o.purchase_amt) as totalSales " +
                     "FROM salesman s " +
                     "JOIN orders o ON s.salesman_id = o.salesman_id " +
                     "GROUP BY s.salesman_id";

        // Define Function to map SQL ResultSet to SalesPerson object
        Function<ResultSet, SalesPerson> mapToSalesPerson = rs -> {
            try {
                return new SalesPerson(
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getDouble("commission"),
                    rs.getDouble("totalSales")
                );
            } catch (SQLException e) {
                throw new RuntimeException("Error mapping result set", e);
            }
        };

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salesPersonList.add(mapToSalesPerson.apply(rs));
            }

            // 1. TABLE: total earnings
            System.out.println("Table 1: Total Earnings (Sales * Commission Rate)");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-20s | %-15s\n", "Salesperson Name", "Total Earnings");
            System.out.println("--------------------------------------------------");
            
            salesPersonList.stream()
                .forEach(sp -> System.out.printf("%-20s | $%-15.2f\n", 
                    sp.getName(), sp.getTotalEarnings()));

            System.out.println("\n");

            // 2. TABLE: total commissions
            System.out.println("Table 2: Total Commissions");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-20s | %-15s\n", "Salesperson Name", "Commission Amount");
            System.out.println("--------------------------------------------------");
            
            salesPersonList.stream()
                .forEach(sp -> System.out.printf("%-20s | $%-15.2f\n", 
                    sp.getName(), sp.getTotalCommissions()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class SalesPerson {
    private String name;
    private String city;
    private double commission;
    private double totalSales;

    // Constructor
    public SalesPerson(String name, String city, double commission, double totalSales) {
        this.name = name;
        this.city = city;
        this.commission = commission;
        this.totalSales = totalSales;
    }

    // Getters
    public String getName() { return name; }
    public double getTotalSales() { return totalSales; }
    
    public double getTotalEarnings() {
        return totalSales * commission;
    }

    public double getTotalCommissions() {
        return totalSales * commission;
    }

    @Override
    public String toString() {
        return String.format("Name: %-15s | City: %-10s | Sales: %10.2f", name, city, totalSales);
    }
}
