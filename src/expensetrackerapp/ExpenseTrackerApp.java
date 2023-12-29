/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package expensetrackerapp;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;


public class ExpenseTrackerApp extends JFrame {
    private JTextField expenseNameField, amountField, dateField;
    private JComboBox<String> categoryDropdown;
    private DefaultTableModel tableModel;

    private Map<String, Double> categoryExpenses = new HashMap<>();
    private Map<String, Double> dayExpenses = new HashMap<>();

    public ExpenseTrackerApp() {
        setTitle("Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadExpenseData(); // Load existing data from file

        setVisible(true);
    }

    private void initComponents() {
        // UI Components
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        expenseNameField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();
        categoryDropdown = new JComboBox<>(new String[]{"Food", "Transportation", "Entertainment"});
        JButton addExpenseButton = new JButton("Add Expense");
        JButton editExpenseButton = new JButton("Edit Expense");
        JButton deleteExpenseButton = new JButton("Delete Expense");
        JButton showStatsButton = new JButton("Show Statistics");
        JButton addCategory = new JButton("Add Category");

        // Table for displaying expenses
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Expense Name");
        tableModel.addColumn("Expense Date");
        tableModel.addColumn("Category");
        tableModel.addColumn("Amount");
        


        JTable expenseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);

        // Add components to the input panel
        inputPanel.add(new JLabel("Expense Name:"));
        inputPanel.add(expenseNameField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryDropdown);
        inputPanel.add(addExpenseButton);
        inputPanel.add(editExpenseButton);
//        inputPanel.add(addCategory);

        // Button listeners
        addExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        JPanel inputPanel2 = new JPanel(new GridLayout(1, 2));
        // Add input panel and table to the main frame
        inputPanel2.add(deleteExpenseButton);
        inputPanel2.add(addCategory);
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(inputPanel2, BorderLayout.SOUTH);
        
        add(showStatsButton, BorderLayout.EAST);

        // Show statistics button listener
        showStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStatistics();
            }
        });

        // Delete expense button listener
        deleteExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExpense();
            }
        });
        
        addCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for updating the category dropdown
                // You can prompt the user to enter a new category and add it to the dropdown
                String newCategory = JOptionPane.showInputDialog(inputPanel, "Enter a new category:");
                if (newCategory != null && !newCategory.trim().isEmpty()) {
                    categoryDropdown.addItem(newCategory);
                    categoryDropdown.setSelectedItem(newCategory);
                }
            }
        });
    }

    private void addExpense() {
        String expenseName = expenseNameField.getText();
        String amountStr = amountField.getText();
        String date = dateField.getText();
        String category = categoryDropdown.getSelectedItem().toString();

        if (expenseName.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);

            // Update category expenses
            categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0.0) + amount);

            // Update day expenses
            dayExpenses.put(date, dayExpenses.getOrDefault(date, 0.0) + amount);

            // Add to table
            tableModel.addRow(new Object[]{expenseName, date, category, amount});

            // Clear input fields
            expenseNameField.setText("");
            amountField.setText("");
            dateField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter a valid number.");
        }
    }

    private void deleteExpense() {
        int selectedRow = tableModel.getRowCount() > 0 ? tableModel.getRowCount() - 1 : -1;
        if (selectedRow >= 0) {
       
            // Remove from table
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "No expense to delete.");
        }
    }

    private void showStatistics() {
        // Show statistics in a new window
        JFrame statsFrame = new JFrame("Expense Statistics");
        statsFrame.setSize(400, 300);

        // Display category expenses in a bar chart
        BarChart categoryChart = new BarChart("Category Expenses", "Category", "Amount");
        for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            categoryChart.addData(entry.getKey(), entry.getValue());
        }

        // Display day expenses in a bar chart
        BarChart dayChart = new BarChart("Day Expenses", "Day", "Amount");
        for (Map.Entry<String, Double> entry : dayExpenses.entrySet()) {
            dayChart.addData(entry.getKey(), entry.getValue());
        }

        // Add charts to the stats frame
        statsFrame.setLayout(new GridLayout(2, 1));
        statsFrame.add(categoryChart.getChartPanel());
        statsFrame.add(dayChart.getChartPanel());

        statsFrame.setVisible(true);
    }

    private void loadExpenseData() {
        // Load data from file (if exists)
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("expenses.dat"))) {
            // Read category expenses and day expenses
            categoryExpenses = (Map<String, Double>) ois.readObject();
            dayExpenses = (Map<String, Double>) ois.readObject();

            // Read expenses and add to the table
            ArrayList<Object[]> expenses = (ArrayList<Object[]>) ois.readObject();
            for (Object[] expense : expenses) {
                tableModel.addRow(expense);
            }
        } catch (FileNotFoundException e) {
            // File not found, ignore
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveExpenseData() {
        // Save data to file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("expenses.dat"))) {
            oos.writeObject(categoryExpenses);
            oos.writeObject(dayExpenses);

            // Save expenses
            ArrayList<Object[]> expenses = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                expenses.add(new Object[]{
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2)
                });
            }
            oos.writeObject(expenses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTrackerApp());
    }

    // BarChart class for creating bar charts
    private static class BarChart {
        private DefaultCategoryDataset dataset;
        private String title;
        private String xAxisLabel;
        private String yAxisLabel;

        public BarChart(String title, String xAxisLabel, String yAxisLabel) {
            this.title = title;
            this.xAxisLabel = xAxisLabel;
            this.yAxisLabel = yAxisLabel;
            this.dataset = new DefaultCategoryDataset();
        }

        public void addData(String category, double value) {
            dataset.addValue(value, title, category);
        }

        public ChartPanel getChartPanel() {
            JFreeChart chart = ChartFactory.createBarChart(
                    title,
                    xAxisLabel,
                    yAxisLabel,
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false
            );

            return new ChartPanel(chart);
        }
    }
}
