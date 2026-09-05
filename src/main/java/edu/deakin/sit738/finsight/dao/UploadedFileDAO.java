package edu.deakin.sit738.finsight.dao;

import java.util.List;
import edu.deakin.sit738.finsight.entity.UploadedFile;

public interface UploadedFileDAO {

    void save(UploadedFile file);

    List<UploadedFile> findByUserId(int userId);

    void delete(int id);
}