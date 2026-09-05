package edu.deakin.sit738.finsight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.deakin.sit738.finsight.dao.UploadedFileDAO;
import edu.deakin.sit738.finsight.entity.UploadedFile;

@Service
public class UploadedFileService {

    @Autowired
    private UploadedFileDAO uploadedFileDAO;

    @Transactional
    public void saveFile(UploadedFile file) {
        uploadedFileDAO.save(file);
    }

    @Transactional(readOnly = true)
    public List<UploadedFile> getFilesByUserId(int userId) {
        return uploadedFileDAO.findByUserId(userId);
    }

    @Transactional
    public void deleteFile(int id) {
        uploadedFileDAO.delete(id);
    }
}