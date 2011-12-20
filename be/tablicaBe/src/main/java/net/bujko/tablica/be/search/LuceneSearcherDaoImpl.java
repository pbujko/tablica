/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

import java.io.IOException;
import java.util.Map;
import net.bujko.tablica.be.model.Ad;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.bujko.tablica.be.model.Category;
import net.bujko.tablica.be.categs.CategoryManager;
import net.bujko.tablica.be.dao.AdDao;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
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
    Logger logger = LoggerFactory.getLogger(LuceneSearcherDaoImpl.class);
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
    Directory index = new RAMDirectory();
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
    public synchronized void add(Ad item) throws Exception {
        Document doc = new Document();
        doc.add(new Field(FIELD_ID, item.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FIELD_HASHEDID, item.getHashedId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FIELD_TITLE, item.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_DESCRIPTION, item.getTitle(), Field.Store.YES, Field.Index.ANALYZED));

        for (Category c : item.getAssignedCategories()) {
            doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        //delete old
        delete(item);

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

        String escaped = LUCENE_PATTERN.matcher(searchQ).replaceAll(REPLACEMENT_STRING);

        Query q = new QueryParser(Version.LUCENE_35, FIELD_CAT_NAME, analyzer).parse(escaped);
        IndexReader reader = IndexReader.open(index, true);
        // 3. search

        IndexSearcher searcher = new IndexSearcher(reader);

        ScoreDoc[] hits = searcher.search(q, 100).scoreDocs;

        List ret =
                new ArrayList<Ad>(hits.length);

        for (ScoreDoc sd : hits) {

            Document d = searcher.doc(sd.doc);
            Ad item = new Ad();
            item.setId(d.get(FIELD_ID));
            for (Fieldable f : d.getFieldables(FIELD_CAT_NAME)) {
                Category tmpC = cm.getCategoryById(f.stringValue());
                if (tmpC != null) {
                    item.addCategory(tmpC);
                } else {
                    logger.error("UNKNWNCAT: {}, itemId: {}", f.stringValue(), item.getId());
                }

            }

            ret.add(item);
        }

        reader.close();
        searcher.close();
        return ret;
    }

    @Override
    public void update(Ad item) throws Exception {

        Document doc = new Document();
        doc.add(new Field(FIELD_ID, item.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FIELD_HASHEDID, item.getHashedId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(FIELD_TITLE, item.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(FIELD_DESCRIPTION, item.getTitle(), Field.Store.YES, Field.Index.ANALYZED));

        for (Category c : item.getAssignedCategories()) {
            doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
        IndexWriter w = obtainWriter();
        w.updateDocument(new Term(FIELD_ID, item.getId()), doc);
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
        for (Ad item : adDao.listAll()) {
            Document doc = new Document();
            doc.add(new Field(FIELD_ID, item.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field(FIELD_HASHEDID, item.getHashedId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field(FIELD_TITLE, item.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field(FIELD_DESCRIPTION, item.getTitle(), Field.Store.YES, Field.Index.ANALYZED));

            for (Category c : item.getAssignedCategories()) {
                doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            }

            tmpWriter.addDocument(doc);
            cnt++;
        }
        tmpWriter.commit();
        tmpWriter.close();

        this.index = tmpIndex;
        logger.info("rebuild end, added {} entries", cnt);
    }

    /**
     * cat|value -> cat:value
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
            sb.append(ISearcherDao.FIELD_CAT_NAME).append(":").append(params.get("cat"));
        }

        return sb.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rebuild();
        logger.info("init completed");
    }
}
