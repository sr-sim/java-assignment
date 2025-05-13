package java_assignment2025;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class FinanceReport {
    private final PurchaseOrderManager poManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FinanceReport(PurchaseOrderManager poManager) {
        this.poManager = poManager;
    }

    // ✅ Returns all paid POs
    public List<PurchaseOrder> getPaidOrders() {
        return poManager.getpolist().stream()
                .filter(po -> "paid".equalsIgnoreCase(po.getPaymentStatus()))
                .collect(Collectors.toList());
    }

    // ✅ Filter by Month (e.g., "May")
    public List<PurchaseOrder> filterByMonth(String monthName) {
        return getPaidOrders().stream()
                .filter(po -> {
                    try {
                        Date date = dateFormat.parse(po.getOrderDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
                        return month.equalsIgnoreCase(monthName);
                    } catch (ParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // ✅ Filter by Week number (e.g., "1", "2", ...)
    public List<PurchaseOrder> filterByWeek(String weekNumberStr) {
        int targetWeek = Integer.parseInt(weekNumberStr);
        return getPaidOrders().stream()
                .filter(po -> {
                    try {
                        Date date = dateFormat.parse(po.getOrderDate());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int week = cal.get(Calendar.WEEK_OF_MONTH);
                        return week == targetWeek;
                    } catch (ParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // ✅ Calculate total amount for a list of POs
    public double calculateTotalAmount(List<PurchaseOrder> poList) {
        return poList.stream()
                .mapToDouble(PurchaseOrder::getAmount)
                .sum();
    }
}
