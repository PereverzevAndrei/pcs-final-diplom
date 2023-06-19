import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {

    protected Map<PageEntry, String> searchReadyUnsorted = new HashMap<>();
    protected Map<PageEntry, String> searchReadySorted = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        String fileName;
        Path directory = Path.of("pdfs/");
        try (DirectoryStream<Path> files = Files.newDirectoryStream(directory)) {

            for (Path path : files) {
                System.out.println(path);
                fileName = String.valueOf(path);
                // first step - reading
                var doc = new PdfDocument(new PdfReader(fileName));
                int pages = doc.getNumberOfPages();
                Map<String, Integer> freqs = new HashMap<>();
                Map<Integer, String> softSkills = new HashMap<>();
                for (int i = 1; i < pages; i++) {
                    int actPage = i;
                    PdfPage page = doc.getPage(i);
                    var text = PdfTextExtractor.getTextFromPage(page);
                    softSkills.put(i, text);
                }
// second step - counting words
                Iterator<Map.Entry<Integer, String>> iterator = softSkills.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, String> entry = iterator.next();
                    Integer key = entry.getKey();
                    String value = entry.getValue();

                    var words = value.split("\\P{IsAlphabetic}+");
                    for (var word : words) { // перебираем слова
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }
                    String finalFileName = fileName;
                    freqs.forEach((k, v) -> {
                        searchReadyUnsorted.put(new PageEntry(finalFileName, key, v), k);
                    });
                    freqs.clear();
                }
            }
        }
        searchReadySorted = searchReadyUnsorted.entrySet().stream()
                .sorted(Comparator.comparing(e -> -e.getKey().getCount()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));
    }

    @Override
    public List<PageEntry> search(String word) {
        String keyword;
        keyword = word.toLowerCase();
        List<PageEntry> searchResult = new ArrayList<>();

        Iterator<Map.Entry<PageEntry, String>> entries = searchReadySorted.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<PageEntry, String> entry = (Map.Entry) entries.next();
            PageEntry key = entry.getKey();
            String value = entry.getValue();
            if (value.equals(word)) {
                searchResult.add(key);
            }
        }
        return searchResult;
    }
}

