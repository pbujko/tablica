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
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
    final String FIELD_DESCRIPTION = "description";
    final String FIELD_ASSIGNED_CAT = "assignedCat";
    Logger logger = LoggerFactory.getLogger(LuceneSearcherDaoImpl.class);
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
    Directory index = new RAMDirectory();
    private Map<String, Object> summary = new HashMap<String, Object>();
    @Autowired
    CategoryManager cm;
    AdDao adDao;

    @Autowired
    LuceneSearcherDaoImpl(AdDao addao) throws Exception {
        this.adDao = addao;

    }

    private IndexWriter obtainWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        return new IndexWriter(index, conf);
    }

    @Override
    public synchronized void add(Ad ad) throws Exception {
        Document doc = new Document();
        doc.add(new Field(FIELD_ID, ad.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FIELD_HASHEDID, ad.getHashedId(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field(FIELD_TITLE, ad.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_DESCRIPTION, ad.getDescription(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_ASSIGNED_CAT, ad.getCategory().getId(), Field.Store.YES, Field.Index.NO));


        for (Category c : ad.getAssignedCategories()) {
            doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        //delete old
        delete(ad);

        IndexWriter w = obtainWriter();
        w.addDocument(doc);
        w.commit();
        w.close();
    }

    @Override
    public void delete(Ad item) throws Exception {
        IndexWriter w = obtainWriter();
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

        Query q = new QueryParser(Version.LUCENE_35, FIELD_CAT_NAME, analyzer).parse(searchQ);
        IndexReader reader = IndexReader.open(index, true);
        // 3. search

        IndexSearcher searcher = new IndexSearcher(reader);

        ScoreDoc[] hits = searcher.search(q, 100).scoreDocs;

        List retL =
                new ArrayList<Ad>(hits.length);

        for (ScoreDoc sd : hits) {

            Document d = searcher.doc(sd.doc);
            Ad ad = new Ad();
            ad.setId(d.get(FIELD_ID));
            ad.setTitle(d.get(FIELD_TITLE));
            ad.setHashedId(d.get(FIELD_HASHEDID));
            ad.setDescription(d.get(FIELD_DESCRIPTION));
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
        doc.add(new Field(FIELD_TITLE, ad.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_DESCRIPTION, ad.getDescription(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_ASSIGNED_CAT, ad.getCategory().getId(), Field.Store.YES, Field.Index.NO));

        for (Category c : ad.getAssignedCategories()) {
            doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        IndexWriter w = obtainWriter();
        w.updateDocument(new Term(FIELD_ID, ad.getId()), doc);
        w.commit();
        w.close();
    }

    @Override
    public synchronized void rebuild() throws Exception {
        logger.info("rebuild start...");

        Directory tmpIndex = new RAMDirectory();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);

        IndexWriter tmpWriter = new IndexWriter(tmpIndex, conf);
        int cnt = 0;
        for (Ad ad : adDao.listAll()) {
            Document doc = new Document();
            doc.add(new Field(FIELD_ID, ad.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field(FIELD_HASHEDID, ad.getHashedId(), Field.Store.YES, Field.Index.NO));
            doc.add(new Field(FIELD_TITLE, ad.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field(FIELD_DESCRIPTION, ad.getDescription(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field(FIELD_ASSIGNED_CAT, ad.getCategory().getId(), Field.Store.YES, Field.Index.NO));

            for (Category c : ad.getAssignedCategories()) {
                doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
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
     * cat|value -> cat:"value"  - cudzyslow potrzebny zeby wyszukiwanie exact
     * 
     * @param params
     * @return 
     */
    @Override
    public String buildQuery(Map<String, String> params) {
        if (params == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        if (params.containsKey("cat")) {
            sb.append(ISearcherDao.FIELD_CAT_NAME).append(":\"").append(params.get("cat")).append("\"");
        }

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
