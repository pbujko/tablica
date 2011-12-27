/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

import java.io.IOException;
import java.util.Map;
import net.bujko.tablica.be.model.Ad;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import net.bujko.tablica.be.model.Category;
import net.bujko.tablica.be.categs.CategoryManager;
import net.bujko.tablica.be.dao.AdDao;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pbujko
 */
@Repository("searchDao")
public final class LuceneSearcherDaoImpl implements ISearcherDao, InitializingBean {

    final String FIELD_ID = "id";
    final String FIELD_HASHEDID = "hashId";
    final String FIELD_TITLE = "title";
    final String FIELD_TITLE_STORE = "titleStored";
    final String FIELD_DESCRIPTION = "description";
    final String FIELD_DESCRIPTION_STORE = "descriptionStore";
    final String FIELD_ASSIGNED_CAT = "assignedCat";
    final String FIELD_ATTR_CHOICES = "attChoice";
    Logger logger = LoggerFactory.getLogger(LuceneSearcherDaoImpl.class);
    Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_35);
    //Directory index = new RAMDirectory();
    Directory index = FSDirectory.open(new java.io.File("/Users/pbujko/Documents/tmp/lucene"));
    private Map<String, Object> summary = new HashMap<String, Object>();
    @Autowired
    CategoryManager cm;
    AdDao adDao;

    @Autowired
    LuceneSearcherDaoImpl(AdDao addao) throws Exception {
        this.adDao = addao;

    }

    private IndexWriter obtainWriter(boolean createNew) throws CorruptIndexException, LockObtainFailedException, IOException {
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        if (createNew) {
            conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            conf.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
        }
        return new IndexWriter(index, conf);
    }

    @Override
    public synchronized void add(Ad ad) throws Exception {
        Document doc = new Document();
        doc.add(new Field(FIELD_ID, ad.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FIELD_HASHEDID, ad.getHashedId(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(FIELD_TITLE, ad.getTitle().toLowerCase(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_TITLE_STORE, ad.getTitle(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(FIELD_DESCRIPTION, ad.getDescription().toLowerCase(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_DESCRIPTION_STORE, ad.getDescription(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(FIELD_ASSIGNED_CAT, ad.getCategory().getId(), Field.Store.YES, Field.Index.NO));


        for (Category c : ad.getAssignedCategories()) {
            doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }


        for (String attId : ad.getChoices().keySet()) {
            doc.add(new Field(FIELD_ATTR_CHOICES, attId + "|" + (ad.getChoices().get(attId)), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        //delete old
        delete(ad);

        IndexWriter w = obtainWriter(false);
        w.addDocument(doc);
        w.commit();
        w.close();
    }

    @Override
    public void delete(Ad item) throws Exception {
        IndexWriter w = obtainWriter(false);
        w.deleteDocuments(new Term(FIELD_ID, item.getId()));
        w.commit();
        w.close();
    }

    @Override
    public List<Ad> search(String searchQ) throws Exception {

        String LUCENE_ESCAPE_CHARS = "[\\\\+\\-\\!\\(\\)\\:\\^\\]\\{\\}\\~\\*\\?]";
        Pattern LUCENE_PATTERN = Pattern.compile(LUCENE_ESCAPE_CHARS);
        String REPLACEMENT_STRING = "\\\\$0";

        // String escaped = LUCENE_PATTERN.matcher(searchQ).replaceAll(REPLACEMENT_STRING);

        Query q = new QueryParser(Version.LUCENE_35, FIELD_TITLE, analyzer).parse(searchQ);
        IndexReader reader = IndexReader.open(index, true);
        // 3. search

        IndexSearcher searcher = new IndexSearcher(reader);

        ScoreDoc[] hits = searcher.search(q, 100).scoreDocs;

        List retL =
                new ArrayList<Ad>(hits.length);
        logger.debug("hits: {}", hits.length);
        for (ScoreDoc sd : hits) {
            logger.debug("doc {}", sd);

            Document d = searcher.doc(sd.doc);
            Ad ad = new Ad();
            ad.setId(d.get(FIELD_ID));
            ad.setTitle(d.get(FIELD_TITLE_STORE));
            ad.setHashedId(d.get(FIELD_HASHEDID));
            ad.setDescription(d.get(FIELD_DESCRIPTION_STORE));
//            for (Fieldable f : d.getFieldables(FIELD_CAT_NAME)) {
//                Category tmpC = cm.getCategoryById(f.stringValue());
//                if (tmpC != null) {
//                    ad.addCategory(tmpC);
//                } else {
//                    logger.error("UNKNWNCAT: {}, itemId: {}", f.stringValue(), ad.getId());
//                }                
//            }
            ad.addCategory(cm.getCategoryById(d.get(FIELD_ASSIGNED_CAT)));

            retL.add(ad);
        }

        reader.close();
        searcher.close();
        return retL;
    }

    @Override
    public void update(Ad ad) throws Exception {

        Document doc = new Document();
        doc.add(new Field(FIELD_ID, ad.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FIELD_HASHEDID, ad.getHashedId(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(FIELD_TITLE, ad.getTitle().toLowerCase(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_TITLE_STORE, ad.getTitle(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(FIELD_DESCRIPTION, ad.getDescription().toLowerCase(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_DESCRIPTION_STORE, ad.getDescription(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(FIELD_ASSIGNED_CAT, ad.getCategory().getId(), Field.Store.YES, Field.Index.NO));

        for (Category c : ad.getAssignedCategories()) {
            doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        IndexWriter w = obtainWriter(false);
        w.updateDocument(new Term(FIELD_ID, ad.getId()), doc);
        w.commit();
        w.close();
    }

    @Override
    public synchronized void rebuild() throws Exception {
        logger.info("rebuild start...");

//        Directory tmpIndex = new RAMDirectory();
        Directory tmpIndex = FSDirectory.open(new java.io.File("/Users/pbujko/Documents/tmp/lucene"));

        /**
        
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        IndexWriter tmpWriter = new IndexWriter(tmpIndex, conf);
         * */
        IndexWriter tmpWriter = obtainWriter(true);
        int cnt = 0;
        for (Ad ad : adDao.listAll()) {
            Document doc = new Document();
            doc.add(new Field(FIELD_ID, ad.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field(FIELD_HASHEDID, ad.getHashedId(), Field.Store.YES, Field.Index.NO));
            doc.add(new Field(FIELD_TITLE, ad.getTitle().toLowerCase(), Field.Store.NO, Field.Index.ANALYZED));
            doc.add(new Field(FIELD_TITLE_STORE, ad.getTitle(), Field.Store.YES, Field.Index.NO));
            doc.add(new Field(FIELD_DESCRIPTION, ad.getDescription().toLowerCase(), Field.Store.NO, Field.Index.ANALYZED));
            doc.add(new Field(FIELD_DESCRIPTION_STORE, ad.getDescription(), Field.Store.YES, Field.Index.NO));
            doc.add(new Field(FIELD_ASSIGNED_CAT, ad.getCategory().getId(), Field.Store.YES, Field.Index.NO));

            for (Category c : ad.getAssignedCategories()) {
                doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            }

            for (String attId : ad.getChoices().keySet()) {
                doc.add(new Field(FIELD_ATTR_CHOICES, attId + "|" + (ad.getChoices().get(attId)), Field.Store.YES, Field.Index.NOT_ANALYZED));
            }

            tmpWriter.addDocument(doc);
            cnt++;
        }
        tmpWriter.commit();
        tmpWriter.close();

        this.index = tmpIndex;
        logger.info("rebuild end, added {} entries", cnt);
        updateSummary();
    }

    /**
     * cat:value -> cat:"value"  - cudzyslow potrzebny zeby wyszukiwanie exact
     * att:attId|choiceId -> attChoice:"ATTID|CHOICEID"  - mozliwe 0 lub wiecej takich wpisow, wszystko laczone operatorem AND
     * @param params
     * @return 
     */
    @Override
    public String buildQuery(Map<String, String> params) {
        if (params == null) {
            return "";
        }
        logger.debug("build q {}", params);
        StringBuilder sb = new StringBuilder();

        if (params.containsKey("cat")) {
            sb.append("+" + ISearcherDao.FIELD_CAT_NAME).append(":\"").append(params.get("cat")).append("\"");
        }


        if (params.containsKey("phrase")) {
            sb.append(" AND (");
            for (String word : params.get("phrase").split(" ")) {
                sb.append(" ").append(FIELD_TITLE).append(":").append(word.toLowerCase()).append("*");
                sb.append(" ").append(FIELD_DESCRIPTION).append(":").append(word.toLowerCase()).append("*");
            }
            sb.append(")");
        }

        for (String k : params.keySet()) {
            if (k.startsWith("attChoice")) {
                sb.append(" AND +").append(FIELD_ATTR_CHOICES).append(":\"").append(params.get(k)).append("\"");
            }
        }

        logger.debug("bq - q: {}", sb);
        return sb.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rebuild();
        logger.info("init completed");
    }

    @Override
    public Map<String, Object> getSummary() {
        return summary;
    }

    private synchronized void updateSummary() {
        logger.debug("start updating summary...");


        try {
            Map<String, Object> newSummary = new HashMap<String, Object>();

            //liczba ogloszen w kategoriach
            for (Category tmpC : cm.getAllCategories()) {
                int cnt = this.search("cat:\"" + tmpC.getId() + "\"").size();
                newSummary.put(STATUS_KEY_ADSPERCAT + tmpC.getId(), cnt);
                logger.debug("{} -> {}", tmpC.getId(), cnt);
            }

            int totalAds = 0;
            //liczba ogloszen total:
            for (Category tmpC : cm.getTopLevelCategories()) {
                totalAds += (Integer) newSummary.get(STATUS_KEY_ADSPERCAT + tmpC.getId());
            }

            newSummary.put(STATUS_KEY_TOTALADS, totalAds);
            logger.debug("counted total Ads {}", totalAds);

            this.summary = newSummary;
        } catch (Exception e) {
            logger.error("UPDSUMM", e);
        }
        logger.debug("end updating summary...");
    }
}
