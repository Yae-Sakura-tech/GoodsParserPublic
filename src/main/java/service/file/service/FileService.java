package service.file.service;

import java.io.File;

public interface FileService {
    boolean writeCSVFile(File file, String csv);
}
