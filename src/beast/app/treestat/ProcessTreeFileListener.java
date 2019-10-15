package beast.app.treestat;

/**
 * Created by adru001 on 30/07/19.
 */
public interface ProcessTreeFileListener {

    void startProcessing();

    void processingHalted();

    void processingComplete(int numTreesProcessed);

    boolean warning(String message);

    void error(String errorTitle, String errorMessage);

    void progress(String progress);
}
