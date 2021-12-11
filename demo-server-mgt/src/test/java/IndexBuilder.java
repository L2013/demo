import cn.hutool.core.io.resource.ResourceUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sun.corba.se.spi.orb.StringPair;
import com.yinuo.demo.server.mgt.dto.FileDto;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IndexBuilder {

    public static final String FILE_BASE_PATH = "./files";

    private List<FileDto> getFiles() {
        URL url = ResourceUtil.getResource(FILE_BASE_PATH);
        File f = new File(url.getFile());
        return Arrays.stream(f.listFiles())
                .map(t -> new FileDto(t.getName(), readContent(t)))
                .collect(Collectors.toList());
    }

    private String readContent(File f) {
        try {
            return new Tika().parseToString(f);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Test
    public void testBuildIndex() throws IOException {
        Analyzer analyzer = new IKAnalyzer(true);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);


        FieldType ft = new FieldType();
        ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        ft.setStored(true);
        ft.setTokenized(true);
        ft.setStoreTermVectors(true);
        ft.setStoreTermVectorPositions(true);
        ft.setStoreTermVectorOffsets(true);

        String path = System.getProperty("user.dir") + "/indexDir";
        Directory directory = FSDirectory.open(Paths.get(path));
        IndexWriter iw = new IndexWriter(directory, iwc);

        List<FileDto> files = getFiles();
        for (FileDto file : files) {
            Document doc = new Document();
            doc.add(new Field("title", file.getTitle(), ft));
            doc.add(new Field("content", file.getContent(), ft));
            iw.addDocument(doc);
        }

        iw.commit();
        iw.close();
        directory.close();
    }

    @Test
    public void testPdfParser() throws Exception {
        String title = getFiles().stream().filter(t -> t.getTitle().endsWith(".pdf")).findFirst().get().getTitle();
        InputStream stream = ResourceUtil.getStream(FILE_BASE_PATH + File.separator + title);
        // 内容处理器
        BodyContentHandler bodyContentHandler = new BodyContentHandler(10 * 1024 * 1024);
        // 元数据对象
        Metadata metadata = new Metadata();
        // 创建内容解析器
        ParseContext parseContext = new ParseContext();
        PDFParser pdfParser = new PDFParser();

        pdfParser.parse(TikaInputStream.get(stream), bodyContentHandler, metadata, parseContext);
        System.out.println("文件属性:");
        String[] names = metadata.names();
        Arrays.sort(names);
        for (String name : names) {
            System.out.println(name + " " + metadata.get(name));
        }
        System.out.println("PDF内容:");
        System.out.println(bodyContentHandler);
    }

    @Test
    public void testAnalyzer() {
        String st1 = "中华人民共和国简称中国， 是一个拥有13亿人的国家";
        String st2 = "Dogs can not archive a place, eyes can reach;";
        List<StringPair> list1 = getAnalyseResult(st1);
        List<StringPair> list2 = getAnalyseResult(st2);
        System.out.println(ImmutableMap.of("list1", list1, "list2", list2));
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
