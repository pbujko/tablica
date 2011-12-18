/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bujko.tablica.be.search;

import net.bujko.tablica.be.model.Ad;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.bujko.tablica.be.model.Category;
import net.bujko.tablica.be.categs.CategoryManager;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author pbujko
 */
@Repository("searchDao")
public class LuceneSearcherDaoImpl implements ISearcherDao {
    
    private final String FIELD_CAT_NAME = "cat";
    private final String FIELD_ID = "id";
    private final String FIELD_HASHEDID = "hashId";
    private IndexWriter w;
    private IndexReader reader;
    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
    IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);
    Directory index = new RAMDirectory();
    
    @Autowired
    CategoryManager cm;

    public LuceneSearcherDaoImpl() throws Exception {
        
        w = new IndexWriter(index, conf);
     //   cm = new CategoryManager();
    }
    
    @Override
    public void add(Ad item) throws CorruptIndexException, IOException {
        Document doc = new Document();
        doc.add(new Field(FIELD_ID, item.getId(), Field.Store.YES, Field.Index.NO));
        for (Category c : item.getAssignedCategories()) {
            doc.add(new Field(FIELD_CAT_NAME, c.getId(), Field.Store.NO, Field.Index.ANALYZED));
        }
        w.addDocument(doc);
        w.commit();
    }
    
    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<Ad> search(String searchQ) throws Exception {
        
        Query q = new QueryParser(Version.LUCENE_35, FIELD_CAT_NAME, analyzer).parse(searchQ);
        reader = IndexReader.open(index, true);
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
                item.addCategory(cm.getCategoryById(f.stringValue()));
            }
            
            ret.add(item);
        }
           
        reader.close();
        searcher.close();
        return ret;
    }
}
