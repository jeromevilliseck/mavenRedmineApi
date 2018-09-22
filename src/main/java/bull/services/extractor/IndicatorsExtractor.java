package bull.services.extractor;

import java.io.IOException;
import java.util.List;

public interface IndicatorsExtractor<T> {
    public List<T> fetch() throws IOException;
}
