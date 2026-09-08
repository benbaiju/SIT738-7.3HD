package edu.deakin.sit738.finsight.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

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
import edu.deakin.sit738.finsight.util.AppLogger;

@Controller
public class FileUploadController {

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024L * 1024L;

    private static final Set<String> ALLOWED_EXTENSIONS =
            new HashSet<String>(Arrays.asList("csv"));

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            new HashSet<String>(Arrays.asList(
                    "text/csv",
                    "application/csv",
                    "text/plain",
                    "application/vnd.ms-excel"));

    private static final Set<String> BLOCKED_EXTENSIONS =
            new HashSet<String>(Arrays.asList(
                    "exe", "bat", "cmd", "sh", "js", "jar", "war",
                    "class", "jsp", "jspx", "php", "asp", "aspx",
                    "dll", "com", "msi", "ps1", "vbs", "wsf", "html",
                    "htm", "svg"));

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
            Model model) {

        if (userId == null) {
            model.addAttribute("error",
                    "User ID is missing. Please open the upload page again.");
            return "upload";
        }

        model.addAttribute("userId", userId);

        String validationError = validateUpload(file);
        if (validationError != null) {
            AppLogger.warn("Upload validation failed. userId=" + userId
                    + " reason=" + validationError);
            model.addAttribute("error", validationError);
            return "upload";
        }

        String originalFileName =
                sanitizeOriginalFileName(file.getOriginalFilename());
        String storedFileName = buildSafeStoredFileName();

        String uploadDirectory =
                request.getServletContext().getRealPath("/WEB-INF/uploads");

        File directory = new File(uploadDirectory);

        if (!directory.exists() && !directory.mkdirs()) {
            model.addAttribute("error",
                    "Unable to store the uploaded file. Please try again.");
            return "upload";
        }

        File destination = new File(directory, storedFileName);

        try {
            file.transferTo(destination);

            if (!isSafeCsvContent(destination)) {
                destination.delete();
                AppLogger.warn("Upload content validation failed. userId="
                        + userId);
                model.addAttribute("error",
                        "The file content is not a valid CSV statement.");
                return "upload";
            }

            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setUserId(userId);
            uploadedFile.setOriginalFileName(originalFileName);
            uploadedFile.setStoredFileName(storedFileName);
            uploadedFile.setFilePath(destination.getAbsolutePath());
            uploadedFile.setFileType("text/csv");
            uploadedFile.setUploadedAt(new Date());

            uploadedFileService.saveFile(uploadedFile);

            importTransactions(destination, userId);

            AppLogger.info("File uploaded successfully. userId=" + userId
                    + " storedFileName=" + storedFileName);
            model.addAttribute("message",
                    "File uploaded and transactions imported successfully.");

        } catch (Exception e) {
            if (destination.exists()) {
                destination.delete();
            }
            AppLogger.error("Upload processing failed. userId=" + userId
                    + " " + AppLogger.requestContext(request), e);
            model.addAttribute("error",
                    "The file could not be processed. Please upload a valid CSV statement.");
        }

        return "upload";
    }

    private String validateUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "Please select a non-empty CSV file.";
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            return "The file exceeds the maximum allowed size of 5 MB.";
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            return "The uploaded file name is invalid.";
        }

        String baseName = extractBaseName(originalFileName);
        String extension = extractExtension(baseName);

        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension)) {
            return "Only CSV files are allowed.";
        }

        if (containsBlockedExtension(baseName)) {
            return "Executable or dangerous file types are not allowed.";
        }

        String contentType = file.getContentType();
        if (contentType == null
                || !ALLOWED_CONTENT_TYPES.contains(
                        contentType.toLowerCase(Locale.ROOT).trim())) {
            return "The file content type is not allowed. Please upload a CSV file.";
        }

        return null;
    }

    private boolean containsBlockedExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        String nameWithoutCsv = lower;

        if (nameWithoutCsv.endsWith(".csv")) {
            nameWithoutCsv = nameWithoutCsv.substring(
                    0, nameWithoutCsv.length() - 4);
        }

        for (String blocked : BLOCKED_EXTENSIONS) {
            if (nameWithoutCsv.endsWith("." + blocked)
                    || nameWithoutCsv.contains("." + blocked + ".")) {
                return true;
            }
        }

        return false;
    }

    private boolean isSafeCsvContent(File csvFile) throws IOException {
        byte[] sample = new byte[8192];
        int bytesRead;

        try (InputStream inputStream = new FileInputStream(csvFile)) {
            bytesRead = inputStream.read(sample);
        }

        if (bytesRead <= 0) {
            return false;
        }

        for (int i = 0; i < bytesRead; i++) {
            if (sample[i] == 0) {
                return false;
            }
        }

        String sampleText = new String(sample, 0, bytesRead, "UTF-8");
        String[] lines = sampleText.split("\\r?\\n");

        if (lines.length < 1) {
            return false;
        }

        String header = lines[0].toLowerCase(Locale.ROOT);
        return header.contains("date")
                && header.contains("description")
                && header.contains("amount");
    }

    private String buildSafeStoredFileName() {
        return UUID.randomUUID().toString().replace("-", "") + ".csv";
    }

    private String sanitizeOriginalFileName(String originalFileName) {
        String baseName = extractBaseName(originalFileName);
        if (baseName == null || baseName.isEmpty()) {
            return "statement.csv";
        }
        return baseName.replaceAll("[^a-zA-Z0-9._\\- ]", "_");
    }

    private String extractBaseName(String fileName) {
        String normalised = fileName.replace("\\", "/");
        int slashIndex = normalised.lastIndexOf('/');
        if (slashIndex >= 0) {
            normalised = normalised.substring(slashIndex + 1);
        }
        return normalised.trim();
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return null;
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
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

                String amountValue =
                        values[3].trim().replace("$", "").replace(",", "");

                transaction.setAmount(Double.parseDouble(amountValue));

                transaction.setTransactionType(values[4].trim());

                transaction.setTransactionDate(
                        dateFormat.parse(values[5].trim()));

                bankTransactionService.saveTransaction(transaction);
            }

        } catch (Exception e) {
            throw new IOException("CSV import failed", e);
        }
    }
}
