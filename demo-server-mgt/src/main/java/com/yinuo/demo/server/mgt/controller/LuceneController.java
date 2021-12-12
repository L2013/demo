package com.yinuo.demo.server.mgt.controller;

import com.yinuo.base.dto.SingleResponse;
import com.yinuo.demo.server.mgt.consts.PathConstant;
import com.yinuo.demo.server.mgt.dto.FileDto;
import org.apache.commons.compress.utils.Lists;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author liang
 */
@RestController
public class LuceneController {

    @GetMapping(PathConstant.LUCENE)
    public SingleResponse<List<FileDto>> getTopDoc(String key) throws IOException, ParseException {
        //TODO 希望做一个基于lucence的搜索功能
        return null;
    }

    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
        String key = "科学";
        List<FileDto> hits = Lists.newArrayList();
        // 检索域
        String[] fields = {"title", "content"};
        // 索引查询器
        Directory directory = FSDirectory.open(Paths.get(System.getProperty("user.dir") + "/indexDir"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

        // 分词器
        IKAnalyzer analyzer = new IKAnalyzer(true);

        // 构建查询
        Query query = new MultiFieldQueryParser(fields, analyzer).parse(key);

        // 查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        QueryScorer queryScorer = new QueryScorer(query, fields[0]);
        Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter("<span style='color:red'>", "</span>"), queryScorer);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            String title = doc.get("title");
            String content = doc.get("content");
            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
            highlighter.setTextFragmenter(fragmenter);
            TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, fields[0], analyzer);
            String hlTitle = highlighter.getBestFragment(tokenStream, title);
            tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, fields[1], analyzer);
            String hlContent = highlighter.getBestFragment(tokenStream, content);
            FileDto fileDto = new FileDto(hlTitle, hlContent);
            hits.add(fileDto);
        }
        directory.close();
        DirectoryReader.open(directory).close();

        hits.stream().forEach(t -> {
            System.out.println(t.getTitle());
            System.out.println(t.getContent());
        });
    }
}
