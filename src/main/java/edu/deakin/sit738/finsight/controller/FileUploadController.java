package edu.deakin.sit738.finsight.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.deakin.sit738.finsight.entity.BankTransaction;
import edu.deakin.sit738.finsight.entity.UploadedFile;
import edu.deakin.sit738.finsight.service.BankTransactionService;
import edu.deakin.sit738.finsight.service.UploadedFileService;

@Controller
public class FileUploadController {

    @Autowired
    private UploadedFileService uploadedFileService;

    @Autowired
    private BankTransactionService bankTransactionService;

    @GetMapping("/upload")
    public String showUploadPage(
            @RequestParam("userId") int userId,
            Model model) {

        model.addAttribute("userId", userId);

        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request,
            Model model) throws IOException {

        if (userId == null) {
            model.addAttribute("error",
                    "User ID is missing. Please open the upload page again.");
            return "upload";
        }

        if (file.isEmpty()) {
            model.addAttribute("userId", userId);
            model.addAttribute("error", "Please select a file.");
            return "upload";
        }

        String originalFileName = file.getOriginalFilename();

        String uploadDirectory =
                request.getServletContext().getRealPath("/uploads");

        File directory = new File(uploadDirectory);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String storedFileName = originalFileName;

        File destination = new File(directory, storedFileName);

        file.transferTo(destination);

        UploadedFile uploadedFile = new UploadedFile();

        uploadedFile.setUserId(userId);
        uploadedFile.setOriginalFileName(originalFileName);
        uploadedFile.setStoredFileName(storedFileName);
        uploadedFile.setFilePath(destination.getAbsolutePath());
        uploadedFile.setFileType(file.getContentType());
        uploadedFile.setUploadedAt(new Date());

        uploadedFileService.saveFile(uploadedFile);

        importTransactions(destination, userId);

        model.addAttribute("userId", userId);
        model.addAttribute("message",
                "File uploaded and transactions imported successfully.");

        return "upload";
    }

    private void importTransactions(
            File csvFile,
            int userId) throws IOException {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");

        try (
                BufferedReader reader =
                        new BufferedReader(new FileReader(csvFile))
        ) {

            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",", -1);

                if (values.length < 6) {
                    continue;
                }

                BankTransaction transaction =
                        new BankTransaction();

                transaction.setUserId(userId);
                transaction.setTransactionReference(values[0].trim());
                transaction.setDescription(values[1].trim());
                transaction.setCategory(values[2].trim());

                transaction.setAmount(
                        Double.parseDouble(values[3].trim()));

                transaction.setTransactionType(values[4].trim());

                transaction.setTransactionDate(
                        dateFormat.parse(values[5].trim()));

                bankTransactionService.saveTransaction(transaction);
            }

        } catch (Exception e) {

            throw new IOException(
                    "Error importing CSV transactions: "
                            + e.getMessage(), e);
        }
    }
}