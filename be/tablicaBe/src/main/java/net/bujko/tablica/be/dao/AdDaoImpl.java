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
import net.bujko.tablica.be.categs.CategoryManager;
import net.bujko.tablica.be.model.Ad;
import net.bujko.tablica.be.model.Category;
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
    @Autowired
    CategoryManager cm;

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

        //store category

        ps = conn.prepareStatement("replace into ad_categs(ad_id, cat_id) values (?, ?)");

        ps.setInt(1, insertedKeyValue);
        ps.setString(2, ad.getCategory().getId());
        ps.executeUpdate();

        conn.close();

        logger.debug("saved Ad {}", ad);
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
    public Ad findById(String id) throws SQLException, Exception {
        Connection conn = dataSource.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("select *, GROUP_CONCAT(ad_categs.cat_id) as categs "
                    + "from ad inner join ad_categs on ad.ad_id=ad_categs.ad_id "
                    + "where ad.ad_id=? "
                    + "group by ad.ad_id limit 10");
            ps.setInt(1, new Integer(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Ad ad = new Ad();
                ad.setId(rs.getInt("ad_id") + "");
                ad.setHashedId(rs.getString("ad_hashed_id"));
                ad.setTitle(rs.getString("ad_title"));
                ad.setDescription(rs.getString("description"));

                //categs
                if (rs.getString("categs") != null) {

                    for (String sCatId : rs.getString("categs").split(",")) {
                        Category c = cm.getCategoryById(sCatId);
                        ad.addCategory(c);
                    }
                }

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

            ResultSet rs = conn.createStatement().
                    executeQuery("select *, GROUP_CONCAT(ad_categs.cat_id) as categs "
                    + "from ad inner join ad_categs on ad.ad_id=ad_categs.ad_id "
                    + "group by ad.ad_id limit 10");
            while (rs.next()) {
                Ad ad = new Ad();
                ad.setId(rs.getInt("ad_id") + "");
                ad.setHashedId(rs.getString("ad_hashed_id"));
                ad.setTitle(rs.getString("ad_title"));
                ad.setDescription(rs.getString("description"));

                //categs
                if (rs.getString("categs") != null) {
                    //iteracja po kolekcji ale w rzeczywistosci
                    //powinna byc zawsze tylko jedna
                    
                    for (String sCatId : rs.getString("categs").split(",")) {
                        Category c = cm.getCategoryById(sCatId);
                        ad.addCategory(c);
                    }
                }

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
