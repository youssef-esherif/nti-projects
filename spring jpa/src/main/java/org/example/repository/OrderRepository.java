package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.Order;
import org.example.model.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager em;

    public Order save(Order o) {
        if (o.getId() == null) { em.persist(o); return o; }
        return em.merge(o);
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    public Optional<Order> findByIdWithItems(Long id) {
        return em.createQuery(
                        "select distinct o from Order o left join fetch o.items where o.id = :id", Order.class)
                .setParameter("id", id)
                .getResultStream().findFirst();
    }

    public List<Order> findByCustomer(Long customerId) {
        return em.createQuery("select o from Order o where o.customer.id = :c", Order.class)
                .setParameter("c", customerId)
                .getResultList();
    }

    public List<Order> findByStatus(OrderStatus status) {
        return em.createQuery("select o from Order o where o.status = :s", Order.class)
                .setParameter("s", status)
                .getResultList();
    }
}