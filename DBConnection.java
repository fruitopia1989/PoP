import java.sql.*;
import java.util.ArrayList;

// Class to hold data
class Sales {
    int orderNumber;
    String customerName, customerCity, salesmanName;
    double amount, commissionAmount;

    // Constructor that does the math internally
    Sales(int id, String cn, String cc, String sn, double amt, double com) {
        orderNumber = id; 
        customerName = cn; 
        customerCity = cc; 
        salesmanName = sn; 
        amount = amt; 
        commissionAmount = amt * com;
    }
}

public class DBConnection {
    public static void main(String[] args) throws Exception {
        
        String url = "jdbc:mariadb://localhost:3306/" + args[2];
        ArrayList<Sales> list = new ArrayList<>();

        // Join query to get all data at once
        String sql = "SELECT o.order_no, c.customer_name, c.city, s.name, o.purchase_amt, s.commission " +
                     "FROM orders o JOIN customer c ON o.customer_id=c.customer_id " +
                     "JOIN salesman s ON o.salesman_id=s.salesman_id";

        try (Connection c = DriverManager.getConnection(url, args[0], args[1])) {
            ResultSet rs = c.createStatement().executeQuery(sql);
            while (rs.next()) 
                // Create Sales object and add to list
                list.add(new Sales(rs.getInt(1), rs.getString(2), rs.getString(3), 
                                   rs.getString(4), rs.getDouble(5), rs.getDouble(6)));
        } catch (Exception e) {
        e.printStackTrace();
    }

        // Print the objects from the list
        for (Sales s : list) 
            System.out.println(s.orderNumber + " " + s.customerName + " $" + s.commissionAmount);
    }
}