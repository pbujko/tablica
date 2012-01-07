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
import java.util.Arrays;
import java.util.Collection;
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
    private final String COLUMN_AD_CITY = "ad_city",
            COLUMN_AD_PRICE = "ad_price",
            COLUMN_AD_IMG = "ad_img", COLUMN_AD_MODIFIED = "ad_modifyDate", 
            COLUMN_AD_CREATED = "ad_createDate", COLUMN_AD_CONTACT = "ad_contact",
            COLUMN_AD_EMAIL = "ad_email";
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
                + "description,"
                + "ad_hashed_id, "
                + "ad_title, "
                + COLUMN_AD_ADATTS + ","
                + COLUMN_AD_CITY + ","
                + COLUMN_AD_PRICE + ","
                + COLUMN_AD_IMG + ","
                + COLUMN_AD_CONTACT +","
                + COLUMN_AD_EMAIL
                + ") "
                + "values(?,?,?,?,?,  ?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, ad.getDescription());
        ps.setString(2, ad.getHashedId());
        ps.setString(3, ad.getTitle());
        ps.setString(4, marshallAttChoices(ad.getChoices()));
        ps.setString(5, ad.getCity().getId());
        ps.setString(6, ad.getPrice());
        ps.setString(7, marshallImages(ad.getImages()));
        ps.setString(8, ad.getPhone());
        ps.setString(9, ad.getEmail());
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
                ad.setCity(cm.getCityById(rs.getString(COLUMN_AD_CITY)));
                ad.setPrice(rs.getString(COLUMN_AD_PRICE));
                ad.setImages(unmarshallImages(rs.getString(COLUMN_AD_IMG)));
                ad.setCreated(rs.getTimestamp(COLUMN_AD_CREATED));
                if (rs.getTimestamp(COLUMN_AD_MODIFIED) != null) {
                    ad.setModified(rs.getTimestamp(COLUMN_AD_MODIFIED));
                }
                ad.setPhone(rs.getString(COLUMN_AD_CONTACT));
                ad.setEmail(rs.getString(COLUMN_AD_EMAIL));

                return ad;
            } else {
                return null;
            }
        } finally {

            conn.close();
        }
    }

    @Override
    public Ad findByIdAndHashedId(String id, String hashedId) throws Exception {

        Ad retAd = findById(id);
        if (retAd != null && hashedId.equals(retAd.getHashedId())) {
            return retAd;
        } else {
            return null;
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

                ad.setCity(cm.getCityById(rs.getString(COLUMN_AD_CITY)));
                ad.setPrice(rs.getString(COLUMN_AD_PRICE));
                ad.setImages(unmarshallImages(rs.getString(COLUMN_AD_IMG)));
                ad.setCreated(rs.getTimestamp(COLUMN_AD_CREATED));
                if (rs.getTimestamp(COLUMN_AD_MODIFIED) != null) {
                    ad.setModified(rs.getTimestamp(COLUMN_AD_MODIFIED));
                }
//                ad.setPhone(rs.getString(COLUMN_AD_CONTACT));
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

    private String marshallImages(Collection<String> imgIds) {
        if (imgIds == null || imgIds.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String i : imgIds) {
            sb.append(i).append(",");
        }

        return sb.toString();
    }

    private Collection<String> unmarshallImages(String s) {
        Collection<String> retc = new ArrayList<String>();
        if (s == null || s.isEmpty()) {
            return retc;
        }
        retc.addAll(Arrays.asList(s.split(",")));
        return retc;
    }

    /**
     * celowo nie ustawiam wszystkich pol w Ad poniewaz lista wykorzystujaca te wyniki wyswietla tylko kompaktowe info (tytul, cena, miasto, kategoria, id)
     * @param from
     * @param limit
     * @return
     * @throws Exception 
     */
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
//                ad.setDescription(rs.getString("description"));
                ad.setCity(cm.getCityById(rs.getString(COLUMN_AD_CITY)));
                ad.setPrice(rs.getString(COLUMN_AD_PRICE));
                ad.setImages(unmarshallImages(rs.getString(COLUMN_AD_IMG)));
                ad.setCreated(rs.getTimestamp(COLUMN_AD_CREATED));
                if (rs.getTimestamp(COLUMN_AD_MODIFIED) != null) {
                    ad.setModified(rs.getTimestamp(COLUMN_AD_MODIFIED));
                }


                retL.add(ad);
            }

        } finally {
            conn.close();
        }
        return retL;
    }
}
