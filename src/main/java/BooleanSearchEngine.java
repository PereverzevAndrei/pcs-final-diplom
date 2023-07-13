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

public class BooleanSearchEngine implements SearchEngine {
    protected Map<String, List<PageEntry>> searchReadyUnsorted = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        String fileName;
        Path directory = Path.of(String.valueOf(pdfsDir));
        try (DirectoryStream<Path> files = Files.newDirectoryStream(directory)) {

            for (Path path : files) {
                System.out.println(path);
                fileName = String.valueOf(path);
// first step - reading
                var doc = new PdfDocument(new PdfReader(fileName));
                int pages = doc.getNumberOfPages();
                Map<String, Integer> freqs = new HashMap<>();
                List<PageEntry> searchBox = new ArrayList<PageEntry>();
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
                    for (var word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }
                        word = word.toLowerCase();
                        freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                    }
                    String finalFileName = fileName;
//third Step - creating result map
                    freqs.forEach((k, v) -> {
                        if (searchReadyUnsorted.containsKey(k)) {
                            List<PageEntry> entries = searchReadyUnsorted.get(k);
                            entries.add(new PageEntry(finalFileName, key, v));
                         } else
                            searchReadyUnsorted.put(k, new ArrayList<PageEntry>());
                        List<PageEntry> entries = searchReadyUnsorted.get(k);
                        entries.add(new PageEntry(finalFileName, key, v));
                    });
                    freqs.clear();
                }
               searchReadyUnsorted.forEach((k, v) -> {
                    List<PageEntry> entries = searchReadyUnsorted.get(k);
                    entries.sort(PageEntry::compareTo);
                });
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        String keyword = word.toLowerCase();
        List<PageEntry> searchResult = searchReadyUnsorted.get(keyword);
        return searchResult;
    }
}

