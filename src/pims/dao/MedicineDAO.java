package pims.dao;

import pims.model.Medicine;
import pims.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {

    // ==========================================
    // GET ALL MEDICINES
    // ==========================================

    public List<Medicine> getAllMedicines() {

        List<Medicine> medicines = new ArrayList<>();

        String sql = "SELECT * FROM medicines ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                medicines.add(createMedicineFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }


    // ==========================================
    // SEARCH MEDICINES
    // Name, Company, Medicine Type
    // ==========================================

    public List<Medicine> searchMedicines(String searchTerm) {

        List<Medicine> medicines = new ArrayList<>();

        String sql =
                "SELECT * FROM medicines " +
                        "WHERE name LIKE ? " +
                        "OR company LIKE ? " +
                        "OR medicine_type LIKE ? " +
                        "ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String search = "%" + searchTerm + "%";

            pstmt.setString(1, search);
            pstmt.setString(2, search);
            pstmt.setString(3, search);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                medicines.add(createMedicineFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }


    // ==========================================
    // GET MEDICINE BY ID
    // ==========================================

    public Medicine getMedicineById(int id) {

        String sql =
                "SELECT * FROM medicines WHERE medicine_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createMedicineFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // ==========================================
    // ADD MEDICINE
    // ==========================================

    public boolean addMedicine(Medicine medicine) {

        String sql =
                "INSERT INTO medicines " +
                        "(name, company, medicine_type, price, " +
                        "quantity_in_stock, reorder_level, expiry_date, supplier_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, medicine.getName());
            pstmt.setString(2, medicine.getCompany());
            pstmt.setString(3, medicine.getMedicineType());
            pstmt.setDouble(4, medicine.getPrice());
            pstmt.setInt(5, medicine.getQuantityInStock());
            pstmt.setInt(6, medicine.getReorderLevel());
            pstmt.setDate(7, Date.valueOf(medicine.getExpiryDate()));
            pstmt.setInt(8, medicine.getSupplierId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // ==========================================
    // UPDATE MEDICINE
    // ==========================================

    public boolean updateMedicine(Medicine medicine) {

        String sql =
                "UPDATE medicines SET " +
                        "name = ?, " +
                        "company = ?, " +
                        "medicine_type = ?, " +
                        "price = ?, " +
                        "quantity_in_stock = ?, " +
                        "reorder_level = ?, " +
                        "expiry_date = ?, " +
                        "supplier_id = ? " +
                        "WHERE medicine_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, medicine.getName());
            pstmt.setString(2, medicine.getCompany());
            pstmt.setString(3, medicine.getMedicineType());
            pstmt.setDouble(4, medicine.getPrice());
            pstmt.setInt(5, medicine.getQuantityInStock());
            pstmt.setInt(6, medicine.getReorderLevel());
            pstmt.setDate(7, Date.valueOf(medicine.getExpiryDate()));
            pstmt.setInt(8, medicine.getSupplierId());
            pstmt.setInt(9, medicine.getMedicineId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // ==========================================
    // DELETE MEDICINE
    // ==========================================

    public boolean deleteMedicine(int id) {

        String sql =
                "DELETE FROM medicines WHERE medicine_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // ==========================================
    // HELPER
    // ==========================================

    private Medicine createMedicineFromResultSet(
            ResultSet rs
    ) throws SQLException {

        Date expiryDate =
                rs.getDate("expiry_date");

        return new Medicine(
                rs.getInt("medicine_id"),
                rs.getString("name"),
                rs.getString("company"),
                rs.getString("medicine_type"),
                rs.getDouble("price"),
                rs.getInt("quantity_in_stock"),
                rs.getInt("reorder_level"),
                expiryDate != null
                        ? expiryDate.toLocalDate()
                        : null,
                rs.getInt("supplier_id")
        );
    }
}