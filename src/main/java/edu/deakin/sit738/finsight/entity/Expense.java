package edu.deakin.sit738.finsight.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 100,
            message = "Description must be between 1 and 100 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\s.,'\\-]+$",
            message = "Description contains invalid characters"
    )
    private String description;

    @NotBlank(message = "Category is required")
    @Pattern(
            regexp = "^(Housing|Food|Transport|Loans|Other)$",
            message = "Category must be one of: Housing, Food, Transport, Loans, Other"
    )
    private String category;

    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private double amount;

    @Temporal(TemporalType.DATE)
    private Date expenseDate;

    public Expense() {
    }

    public Expense(int userId, String description, String category,
                   double amount, Date expenseDate) {
        this.userId = userId;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }
}
