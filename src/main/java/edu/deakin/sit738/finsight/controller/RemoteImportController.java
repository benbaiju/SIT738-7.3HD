package edu.deakin.sit738.finsight.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.BankTransaction;
import edu.deakin.sit738.finsight.service.BankTransactionService;

@Controller
public class RemoteImportController {

    @Autowired
    private BankTransactionService bankTransactionService;

    @GetMapping("/remote-import")
    public String showRemoteImportPage(
            @RequestParam("userId") int userId,
            Model model) {

        model.addAttribute("userId", userId);

        return "upload";
    }

    @PostMapping("/remote-import")
    public String importFromUrl(
            @RequestParam("userId") int userId,
            @RequestParam("url") String url,
            Model model) {

        try {
            String csvContent = fetchRemoteFile(url);

            importTransactions(csvContent, userId);

            model.addAttribute(
                    "message",
                    "Remote CSV fetched and transactions imported successfully."
            );

        } catch (Exception e) {
            model.addAttribute(
                    "error",
                    "Error importing remote CSV: " + e.getMessage()
            );
        }

        model.addAttribute("userId", userId);

        return "upload";
    }

    private String fetchRemoteFile(String url) throws IOException {

        URL remoteUrl = new URL(url);

        HttpURLConnection connection =
                (HttpURLConnection) remoteUrl.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException(
                    "Remote server returned HTTP status " + responseCode
            );
        }

        StringBuilder csvBuilder = new StringBuilder();

        try (
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(inputStream)
                        )
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                csvBuilder.append(line).append("\n");
            }

        } finally {
            connection.disconnect();
        }

        return csvBuilder.toString();
    }

    private void importTransactions(
            String csvContent,
            int userId
    ) throws IOException, ParseException {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd");

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new StringReader(csvContent)
                        )
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

                transaction.setTransactionReference(
                        values[0].trim()
                );

                transaction.setDescription(
                        values[1].trim()
                );

                transaction.setCategory(
                        values[2].trim()
                );

                transaction.setAmount(
                        Double.parseDouble(values[3].trim())
                );

                transaction.setTransactionType(
                        values[4].trim()
                );

                transaction.setTransactionDate(
                        dateFormat.parse(values[5].trim())
                );

                bankTransactionService.saveTransaction(
                        transaction
                );
            }
        }
    }
}