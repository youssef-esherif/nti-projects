package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dto.CategoryRevenue;
import org.example.dto.CustomerSpend;
import org.example.dto.MonthlySales;
import org.example.model.OrderStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportRepository {

    @PersistenceContext
    private EntityManager em;

    // 1. revenue by category (PAID + SHIPPED only)
    public List<CategoryRevenue> revenueByCategory() {
        return em.createQuery(
                        "select new org.example.dto.CategoryRevenue(c.name, " +
                                "  sum(i.unitPrice * i.quantity)) " +
                                "from Order o join o.items i join i.product.categories c " +
                                "where o.status in :statuses " +
                                "group by c.name order by c.name",
                        CategoryRevenue.class)
                .setParameter("statuses", List.of(OrderStatus.PAID, OrderStatus.SHIPPED))
                .getResultList();
    }

    // 2. top customers by total spend
    public List<CustomerSpend> topCustomers(int limit) {
        return em.createQuery(
                        "select new org.example.dto.CustomerSpend(o.customer.name, " +
                                "  sum(i.unitPrice * i.quantity)) " +
                                "from Order o join o.items i " +
                                "where o.status in :statuses " +
                                "group by o.customer.name " +
                                "order by sum(i.unitPrice * i.quantity) desc",
                        CustomerSpend.class)
                .setParameter("statuses", List.of(OrderStatus.PAID, OrderStatus.SHIPPED))
                .setMaxResults(limit)
                .getResultList();
    }

    // 3. orders per status
    public Map<OrderStatus, Long> ordersPerStatus() {
        List<Object[]> rows = em.createQuery(
                        "select o.status, count(o) from Order o group by o.status", Object[].class)
                .getResultList();
        Map<OrderStatus, Long> map = new LinkedHashMap<>();
        for (Object[] r : rows) map.put((OrderStatus) r[0], (Long) r[1]);
        return map;
    }

    // 4. products never ordered
    public List<String> productsNeverOrdered() {
        return em.createQuery(
                        "select p.name from Product p " +
                                "where not exists (select 1 from OrderItem i where i.product = p)",
                        String.class)
                .getResultList();
    }

    // 5. monthly sales for a year
    public List<MonthlySales> monthlySales(int year) {
        return em.createQuery(
                        "select new org.example.dto.MonthlySales(" +
                                "  month(o.orderedAt), sum(i.unitPrice * i.quantity)) " +
                                "from Order o join o.items i " +
                                "where year(o.orderedAt) = :y and o.status in :statuses " +
                                "group by month(o.orderedAt) order by month(o.orderedAt)",
                        MonthlySales.class)
                .setParameter("y", year)
                .setParameter("statuses", List.of(OrderStatus.PAID, OrderStatus.SHIPPED))
                .getResultList();
    }

    // 6. bulk discount by category
    public int applyDiscount(String category, double percent) {
        BigDecimal factor = BigDecimal.valueOf(1 - percent / 100.0);
        int updated = em.createQuery(
                        "update Product p set p.price = p.price * :f " +
                                "where :cat in (select c.name from p.categories c)")
                .setParameter("f", factor)
                .setParameter("cat", category)
                .executeUpdate();
        em.clear();
        return updated;
    }
}