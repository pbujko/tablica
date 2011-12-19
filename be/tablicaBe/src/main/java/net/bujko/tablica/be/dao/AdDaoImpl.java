/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import net.bujko.tablica.be.model.Ad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pbujko
 */
@Repository("adDao")
public class AdDaoImpl implements AdDao {

    Logger logger = LoggerFactory.getLogger(AdDaoImpl.class);
    @Autowired
    DataSource dataSource;

    @Override
    public void save(Ad ad) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into ad(description,ad_hashed_id, ad_title) values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ad.getDescription());
        ps.setString(2, ad.getHashedId());
        ps.setString(3, ad.getTitle());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int insertedKeyValue = rs.getInt(1);
        ad.setId(insertedKeyValue + "");
        conn.close();
    }

    @Override
    public void update(Ad ad) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Ad ad) throws SQLException {

        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("delete from ad where ad_id=?");
        ps.setInt(1, new Integer(ad.getId()));
        ps.executeUpdate();
        conn.close();
    }

    @Override
    public Ad findById(String id) throws SQLException {
        Connection conn = dataSource.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from ad where ad_id=?");
            ps.setInt(1, new Integer(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Ad ad = new Ad();
                ad.setId(rs.getInt("ad_id") + "");
                ad.setHashedId(rs.getString("ad_hashed_id"));
                ad.setTitle(rs.getString("ad_title"));
                ad.setDescription(rs.getString("description"));
                return ad;
            } else {
                return null;
            }
        } finally {

            conn.close();
        }

    }

    @Override
    public List<Ad> listAll() throws Exception {
        List<Ad> retL = new ArrayList<Ad>();
        Connection conn = dataSource.getConnection();
        try {

            ResultSet rs = conn.createStatement().executeQuery("select * from ad limit 10");
            while (rs.next()) {
                Ad ad = new Ad();
                ad.setId(rs.getInt("ad_id") + "");
                ad.setHashedId(rs.getString("ad_hashed_id"));
                ad.setTitle(rs.getString("ad_title"));
                ad.setDescription(rs.getString("description"));
                retL.add(ad);
            }

            return retL;
        } catch (Exception e) {
            logger.error("LISTALL:", e);
            return new ArrayList<Ad>();
        } finally {
            conn.close();
        }

    }
}
