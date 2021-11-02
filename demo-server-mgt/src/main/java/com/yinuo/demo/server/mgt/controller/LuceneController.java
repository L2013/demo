package com.yinuo.demo.server.mgt.controller;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sun.corba.se.spi.orb.StringPair;
import com.yinuo.base.dto.SingleResponse;
import com.yinuo.demo.server.mgt.consts.PathConstant;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liang
 */
@RestController
public class LuceneController {

    @RequestMapping(PathConstant.LUCENE)
    public SingleResponse<Map<String, List<StringPair>>> lucene() {
        String st1 = "中华人民共和国简称中国， 是一个拥有13亿人的国家";
        String st2 = "Dogs can not archive a place, eyes can reach;";
        List<StringPair> list1 = getAnalyseResult(st1);
        List<StringPair> list2 = getAnalyseResult(st2);
        // appendTokenToStringBuilder(sb, st, new StopAnalyzer());
        ImmutableMap<String, List<StringPair>> map = ImmutableMap.of("list1", list1, "list2", list2);
        return SingleResponse.of(map);
    }

    private List<StringPair> getAnalyseResult(String st) {
        List<Analyzer> analyzers = ImmutableList.of(
                new StandardAnalyzer(),
                new WhitespaceAnalyzer(),
                new SimpleAnalyzer(),
                new CJKAnalyzer(),
                new KeywordAnalyzer(),
                new IKAnalyzer(true)
        );
        return analyzers.stream()
                .map(t -> new StringPair(t.getClass().getName(), Joiner.on("|").join(analyze(t, st))))
                .collect(Collectors.toList());
    }

    private List<String> analyze(Analyzer analyzer, String sentence) {
        List<String> result = new ArrayList<>();
        try {
            StringReader reader = new StringReader(sentence);
            TokenStream tokenStream = analyzer.tokenStream(sentence, reader);
            tokenStream.reset();
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                result.add(charTermAttribute.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            analyzer.close();
        }
        return result;
    }
}
