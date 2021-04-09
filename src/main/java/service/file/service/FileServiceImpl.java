package service.file.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileServiceImpl implements FileService {
    @Override
    public boolean writeCSVFile(File file, String csv) {
        try {
            FileUtils.writeStringToFile(file, csv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
