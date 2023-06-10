import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;


    public PageEntry(String pdfName, int page, int count) {
        this.count = count;
        this.page = page;
        this.pdfName = pdfName;
    }

    public int getCount() {
        return count;
    }

    public List<PageEntry> search(String word) {
        List<PageEntry> searchResult = new ArrayList<>();
        return searchResult;
    }

    @Override
    public String toString() {
        return
                "Файл - " +
                pdfName +
                        ", Страница =" + page +
                        ", встречается раз =" + count

        ;
    }

    @Override
    public int compareTo(PageEntry o) {
        if (this.getCount() > o.getCount()) return 1;
        if (this.getCount() < o.getCount()) return -1;
        else return 0;
    }

}
