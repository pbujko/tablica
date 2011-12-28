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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import net.bujko.tablica.be.categs.CategoryManager;
import net.bujko.tablica.be.model.Ad;
import net.bujko.tablica.be.model.Category;
import org.json.JSONException;
import org.json.JSONObject;
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

    private final String COLUMN_AD_ADATTS = "ad_atts";
    private final String COLUMN_ADD_CITY="ad_city";
    Logger logger = LoggerFactory.getLogger(AdDaoImpl.class);
    @Autowired
    DataSource dataSource;
    @Autowired
    CategoryManager cm;

    @Override
    public void save(Ad ad) throws SQLException {

        logger.trace("saving Ad {}", ad);
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into ad("
                + "description,ad_hashed_id, ad_title, "
                + COLUMN_AD_ADATTS+","
                + COLUMN_ADD_CITY
                + ") "
                + "values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ad.getDescription());
        ps.setString(2, ad.getHashedId());
        ps.setString(3, ad.getTitle());
        ps.setString(4, marshallAttChoices(ad.getChoices()));
        ps.setString(5, ad.getCity().getId());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int insertedKeyValue = rs.getInt(1);
        ad.setId(insertedKeyValue + "");

        //store category
        logger.trace("saving categs, ad:{}, categ {}", ad, ad.getCategory().getId());
        ps = conn.prepareStatement("insert into ad_categs(ad_id, cat_id) values (?, ?)");

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
                ad.addChoices(unmarshallAttChoices(rs.getString(COLUMN_AD_ADATTS)));
                //categs
                if (rs.getString("categs") != null) {

                    for (String sCatId : rs.getString("categs").split(",")) {
                        Category c = cm.getCategoryById(sCatId);
                        ad.addCategory(c);
                    }
                }
                ad.setCity(cm.getCityById(rs.getString(COLUMN_ADD_CITY)));


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
                ad.addChoices(unmarshallAttChoices(rs.getString(COLUMN_AD_ADATTS)));
                //categs
                if (rs.getString("categs") != null) {
                    //iteracja po kolekcji ale w rzeczywistosci
                    //powinna byc zawsze tylko jedna

                    for (String sCatId : rs.getString("categs").split(",")) {
                        Category c = cm.getCategoryById(sCatId);
                        ad.addCategory(c);
                    }
                }

                ad.setCity(cm.getCityById(rs.getString(COLUMN_ADD_CITY)));

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

    private String marshallAttChoices(Map<String, String> attChoices) {

        if (attChoices == null) {
            return "";
        }

        return new org.json.JSONObject(attChoices).toString();
    }

    private Map<String, String> unmarshallAttChoices(String rawString) throws JSONException {
        Map<String, String> retM = new HashMap<String, String>();

        JSONObject o = new JSONObject(rawString);
        Iterator i = o.keys();
        while (i.hasNext()) {

            String k = (String) i.next();
            retM.put(k, o.getString(k));

        }

        return retM;
    }

    @Override
    public List<Ad> listRecent(int from, int limit) throws Exception {

        List<Ad> retL = new ArrayList<Ad>();
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from ad order by ad_createDate desc limit ?,?");
            ps.setInt(1, from);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ad ad = new Ad();
                ad.setId(rs.getInt("ad_id") + "");
                ad.setHashedId(rs.getString("ad_hashed_id"));
                ad.setTitle(rs.getString("ad_title"));
                ad.setDescription(rs.getString("description"));
                ad.setCity(cm.getCityById(rs.getString(COLUMN_ADD_CITY)));
                retL.add(ad);
            }

        } finally {
            conn.close();
        }
        return retL;
    }
}
