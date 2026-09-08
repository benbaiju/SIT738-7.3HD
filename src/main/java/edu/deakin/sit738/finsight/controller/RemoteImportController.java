package edu.deakin.sit738.finsight.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.deakin.sit738.finsight.entity.BankTransaction;
import edu.deakin.sit738.finsight.entity.User;
import edu.deakin.sit738.finsight.service.BankTransactionService;
import edu.deakin.sit738.finsight.util.AppLogger;
import edu.deakin.sit738.finsight.util.SessionAuthUtil;
import edu.deakin.sit738.finsight.util.UrlSafetyUtil;

@Controller
public class RemoteImportController {

    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 10000;
    private static final int MAX_REDIRECTS = 3;
    private static final int MAX_RESPONSE_CHARS = 1024 * 1024;

    @Autowired
    private BankTransactionService bankTransactionService;

    @GetMapping("/remote-import")
    public String showRemoteImportPage(HttpSession session, Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized remote-import page access attempt.");
            return "redirect:/login";
        }

        model.addAttribute("userId", loggedInUser.getId());

        return "upload";
    }

    @PostMapping("/remote-import")
    public String importFromUrl(
            @RequestParam("url") String url,
            HttpSession session,
            Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized remote-import attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        try {

            String response = fetchRemoteFile(url);

            if (isValidCsv(response)) {

                model.addAttribute(
                        "message",
                        "CSV detected. Review the extracted data before importing."
                );

                model.addAttribute("csvPreview", response);
                model.addAttribute("isCsv", true);
                model.addAttribute("url", url);

            } else {

                model.addAttribute(
                        "message",
                        "The response is not a CSV file."
                );
                model.addAttribute("isCsv", false);

            }

        } catch (IllegalArgumentException e) {

            AppLogger.warn("Remote import URL rejected. userId=" + userId
                    + " reason=" + e.getMessage());
            model.addAttribute(
                    "error",
                    "The provided URL is not allowed."
            );

        } catch (Exception e) {

            AppLogger.error("Remote import fetch failed. userId=" + userId, e);
            model.addAttribute(
                    "error",
                    "Unable to fetch the remote file. Please try again."
            );

        }

        model.addAttribute("userId", userId);

        return "upload";
    }

    @PostMapping("/remote-import/save")
    public String saveImportedTransactions(
            @RequestParam("csvContent") String csvContent,
            HttpSession session,
            Model model) {

        User loggedInUser = SessionAuthUtil.getLoggedInUser(session);
        if (loggedInUser == null) {
            AppLogger.warn("Unauthorized remote-import save attempt.");
            return "redirect:/login";
        }

        int userId = loggedInUser.getId();

        try {

            importTransactions(csvContent, userId);

            model.addAttribute(
                    "message",
                    "Transactions imported successfully."
            );

        } catch (Exception e) {

            AppLogger.error("Remote import save failed. userId=" + userId, e);
            model.addAttribute(
                    "error",
                    "Unable to import transactions. Please try again."
            );

        }

        model.addAttribute("userId", userId);

        return "upload";
    }

    private String fetchRemoteFile(String rawUrl) throws Exception {

        String currentUrl = rawUrl.trim();
        int redirectCount = 0;

        while (true) {
            UrlSafetyUtil.validateHttpsUrl(currentUrl);

            URI uri = new URI(currentUrl);
            UrlSafetyUtil.validateResolvedAddresses(uri.getHost());

            URL remoteUrl = uri.toURL();
            HttpURLConnection connection =
                    (HttpURLConnection) remoteUrl.openConnection();

            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/csv,text/plain,*/*");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM
                    || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || responseCode == HttpURLConnection.HTTP_SEE_OTHER
                    || responseCode == 307
                    || responseCode == 308) {

                String location = connection.getHeaderField("Location");
                connection.disconnect();

                if (location == null || location.trim().isEmpty()) {
                    throw new IOException("Redirect location missing.");
                }

                redirectCount++;
                if (redirectCount > MAX_REDIRECTS) {
                    throw new IOException("Too many redirects.");
                }

                URI redirectUri = uri.resolve(location.trim());
                currentUrl = redirectUri.toString();
                continue;
            }

            if (responseCode != HttpURLConnection.HTTP_OK) {
                connection.disconnect();
                throw new IOException("Remote server returned an error status.");
            }

            StringBuilder responseBuilder = new StringBuilder();

            try (
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(inputStream, "UTF-8"))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (responseBuilder.length() + line.length() + 1
                            > MAX_RESPONSE_CHARS) {
                        throw new IOException("Remote response exceeds size limit.");
                    }
                    responseBuilder.append(line).append("\n");
                }
            } finally {
                connection.disconnect();
            }

            return responseBuilder.toString();
        }
    }

    private boolean isValidCsv(String content) {

        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        String[] lines = content.split("\\r?\\n");

        if (lines.length < 2) {
            return false;
        }

        String header = lines[0].toLowerCase();

        return header.contains("date")
                && header.contains("description")
                && header.contains("amount");
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
        }
    }
}
