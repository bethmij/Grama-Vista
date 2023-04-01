package lk.ijse.model;

import lk.ijse.dto.Civil;
import lk.ijse.util.CrudUtil;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CivilModel {

    public static Integer getNextId() throws SQLException {

        ResultSet resultSet = CrudUtil.execute("SELECT reg_number FROM grama_vista.civil ORDER BY reg_number DESC LIMIT 1" );

        if (resultSet.next()) {
            Integer id = resultSet.getInt(1);
            return id+1;
        }
        return 1;
    }




    public static boolean save(Civil civil ) throws SQLException {

        String sql = "INSERT INTO grama_vista.civil (nic, name, address, gender, dob, marriage_status, relation, education_status, school, occupation, working_address, salary) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        boolean isSaved = CrudUtil.execute(sql,
                civil.getNic(),civil.getName(),civil.getAddress(),civil.getGender(),civil.getDob(),civil.getMarriage(),civil.getRelation(),
                civil.getEdu_status(),civil.getSchool(),civil.getOccupation(),civil.getWorking_address(),civil.getSalary());

        return isSaved;


    }

    public static boolean upload(String id, InputStream in) throws SQLException {
        boolean isUploaded = CrudUtil.execute("UPDATE grama_vista.civil SET image=? WHERE reg_number=?",in,id);
        return isUploaded;
    }

    public static List<String> loadCivilId () throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT reg_number FROM grama_vista.civil ");
        List<String> id = new ArrayList<>();

        while (resultSet.next()){
            id.add(resultSet.getString(1));
        }

        return  id;
    }

    public static String searchById(String id) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT name FROM grama_vista.civil WHERE reg_number = ? ", id);
        if(resultSet.next()){
            String name = resultSet.getString(1);
            return name;
        }
        return null;
    }



    public static String getDivisionId(String residence) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT division_id FROM grama_vista.residence WHERE home_id=?",residence);
        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public static String getDivisionId (Integer civil_id) throws SQLException {
        String sql = "SELECT gn_division.division_id FROM grama_vista.residence JOIN grama_vista.multi_residence ON residence.home_id = multi_residence.home_id JOIN grama_vista.gn_division ON residence.division_id = gn_division.division_id WHERE multi_residence.reg_number=?";
        ResultSet resultSet = CrudUtil.execute(sql,civil_id);
        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}

