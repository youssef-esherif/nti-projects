package org.example;

import org.example.library.entity.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class App {
    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("library-pu");

        EntityManager em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            Publisher p1 = new Publisher("Dar El Shorouk");
            Publisher p2 = new Publisher("Dar Al Maaref");

            Category fiction = new Category("Fiction");
            Category classic = new Category("Classic");
            Category drama = new Category("Drama");

            em.persist(p1);
            em.persist(p2);
            em.persist(fiction);
            em.persist(classic);
            em.persist(drama);

            Author a1 = new Author("Naguib Mahfouz");
            Author a2 = new Author("Taha Hussein");

            Book b1 = new Book("Palace Walk");
            Book b2 = new Book("Palace of Desire");
            Book b3 = new Book("Sugar Street");
            Book b4 = new Book("The Days");

            b1.setPublisher(p1);
            b1.addCategory(fiction);
            b1.addCategory(classic);
            b2.setPublisher(p1);
            b2.addCategory(fiction);
            b3.setPublisher(p1);
            b3.addCategory(fiction);
            b4.setPublisher(p2);
            b4.addCategory(drama);

            a1.addBook(b1);
            a1.addBook(b2);
            a1.addBook(b3);
            a2.addBook(b4);

            em.persist(a1);
            em.persist(a2);

            em.getTransaction().commit();

            em.getTransaction().begin();

            List<Book> books = em.createQuery(
                            "SELECT b FROM Book b WHERE b.author.name = :name", Book.class)
                    .setParameter("name", "Naguib Mahfouz")
                    .getResultList();

            books.forEach(b -> System.out.println(b.getTitle()));


            List<Book> byPublisher = em.createQuery("SELECT b FROM Book b WHERE b.publisher.name = :name", Book.class)
                    .setParameter("name", "Dar El Shorouk")
                    .getResultList();
            byPublisher.forEach(b -> System.out.println(b.getTitle()));


            Book b = em.createQuery(
                            "SELECT b FROM Book b WHERE b.id = ?1", Book.class)
                    .setParameter(1, 1L)
                    .getSingleResult();
            System.out.println(b.getTitle());

            Author author = em.createQuery(
                            "SELECT a FROM Author a JOIN FETCH a.books WHERE a.name = :name", Author.class)
                    .setParameter("name", "Naguib Mahfouz")
                    .getSingleResult();
            author.getBooks().forEach(bk -> System.out.println(bk.getTitle()));

            List<Object[]> counts = em.createQuery(
                            "SELECT a.name, COUNT(b) FROM Author a LEFT JOIN a.books b GROUP BY a.name",
                            Object[].class)
                    .getResultList();

            for (Object[] row : counts) {
                System.out.println(row[0] + " -> " + row[1]);
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);
            Root<Book> root = cq.from(Book.class);

            cq.select(root).where(cb.equal(root.get("title"), "Palace Walk"));

            List<Book> result = em.createQuery(cq).getResultList();
            result.forEach(bk -> System.out.println("Criteria: " + bk.getTitle()));


            String titleFilter = "Palace Walk";
            String authorFilter = "Naguib Mahfouz";

            CriteriaBuilder cb2 = em.getCriteriaBuilder();
            CriteriaQuery<Book> cq2 = cb2.createQuery(Book.class);
            Root<Book> root2 = cq2.from(Book.class);

            List<Predicate> preds = new java.util.ArrayList<>();
            if (titleFilter != null)
                preds.add(cb2.equal(root2.get("title"), titleFilter));
            if (authorFilter != null)
                preds.add(cb2.equal(root2.get("author").get("name"), authorFilter));

            cq2.select(root2).where(preds.toArray(new Predicate[0]));

            List<Book> dyn = em.createQuery(cq2).getResultList();
            dyn.forEach(bk -> System.out.println("Dynamic: " + bk.getTitle()));

            em.getTransaction().commit();
            System.out.println("\n\nDone");


        } finally {
            em.close();
            emf.close();
        }
    }
}
