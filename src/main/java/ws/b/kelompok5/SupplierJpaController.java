/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok5;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ws.b.kelompok5.exceptions.IllegalOrphanException;
import ws.b.kelompok5.exceptions.NonexistentEntityException;
import ws.b.kelompok5.exceptions.PreexistingEntityException;

/**
 *
 * @author user
 */
public class SupplierJpaController implements Serializable {

    public SupplierJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Supplier supplier) throws PreexistingEntityException, Exception {
        if (supplier.getBukuCollection() == null) {
            supplier.setBukuCollection(new ArrayList<Buku>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Buku> attachedBukuCollection = new ArrayList<Buku>();
            for (Buku bukuCollectionBukuToAttach : supplier.getBukuCollection()) {
                bukuCollectionBukuToAttach = em.getReference(bukuCollectionBukuToAttach.getClass(), bukuCollectionBukuToAttach.getKodeBuku());
                attachedBukuCollection.add(bukuCollectionBukuToAttach);
            }
            supplier.setBukuCollection(attachedBukuCollection);
            em.persist(supplier);
            for (Buku bukuCollectionBuku : supplier.getBukuCollection()) {
                Supplier oldIdSupplierOfBukuCollectionBuku = bukuCollectionBuku.getIdSupplier();
                bukuCollectionBuku.setIdSupplier(supplier);
                bukuCollectionBuku = em.merge(bukuCollectionBuku);
                if (oldIdSupplierOfBukuCollectionBuku != null) {
                    oldIdSupplierOfBukuCollectionBuku.getBukuCollection().remove(bukuCollectionBuku);
                    oldIdSupplierOfBukuCollectionBuku = em.merge(oldIdSupplierOfBukuCollectionBuku);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSupplier(supplier.getIdSupplier()) != null) {
                throw new PreexistingEntityException("Supplier " + supplier + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Supplier supplier) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier persistentSupplier = em.find(Supplier.class, supplier.getIdSupplier());
            Collection<Buku> bukuCollectionOld = persistentSupplier.getBukuCollection();
            Collection<Buku> bukuCollectionNew = supplier.getBukuCollection();
            List<String> illegalOrphanMessages = null;
            for (Buku bukuCollectionOldBuku : bukuCollectionOld) {
                if (!bukuCollectionNew.contains(bukuCollectionOldBuku)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Buku " + bukuCollectionOldBuku + " since its idSupplier field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Buku> attachedBukuCollectionNew = new ArrayList<Buku>();
            for (Buku bukuCollectionNewBukuToAttach : bukuCollectionNew) {
                bukuCollectionNewBukuToAttach = em.getReference(bukuCollectionNewBukuToAttach.getClass(), bukuCollectionNewBukuToAttach.getKodeBuku());
                attachedBukuCollectionNew.add(bukuCollectionNewBukuToAttach);
            }
            bukuCollectionNew = attachedBukuCollectionNew;
            supplier.setBukuCollection(bukuCollectionNew);
            supplier = em.merge(supplier);
            for (Buku bukuCollectionNewBuku : bukuCollectionNew) {
                if (!bukuCollectionOld.contains(bukuCollectionNewBuku)) {
                    Supplier oldIdSupplierOfBukuCollectionNewBuku = bukuCollectionNewBuku.getIdSupplier();
                    bukuCollectionNewBuku.setIdSupplier(supplier);
                    bukuCollectionNewBuku = em.merge(bukuCollectionNewBuku);
                    if (oldIdSupplierOfBukuCollectionNewBuku != null && !oldIdSupplierOfBukuCollectionNewBuku.equals(supplier)) {
                        oldIdSupplierOfBukuCollectionNewBuku.getBukuCollection().remove(bukuCollectionNewBuku);
                        oldIdSupplierOfBukuCollectionNewBuku = em.merge(oldIdSupplierOfBukuCollectionNewBuku);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = supplier.getIdSupplier();
                if (findSupplier(id) == null) {
                    throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier supplier;
            try {
                supplier = em.getReference(Supplier.class, id);
                supplier.getIdSupplier();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Buku> bukuCollectionOrphanCheck = supplier.getBukuCollection();
            for (Buku bukuCollectionOrphanCheckBuku : bukuCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Supplier (" + supplier + ") cannot be destroyed since the Buku " + bukuCollectionOrphanCheckBuku + " in its bukuCollection field has a non-nullable idSupplier field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(supplier);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Supplier> findSupplierEntities() {
        return findSupplierEntities(true, -1, -1);
    }

    public List<Supplier> findSupplierEntities(int maxResults, int firstResult) {
        return findSupplierEntities(false, maxResults, firstResult);
    }

    private List<Supplier> findSupplierEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Supplier.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Supplier findSupplier(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Supplier.class, id);
        } finally {
            em.close();
        }
    }

    public int getSupplierCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Supplier> rt = cq.from(Supplier.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
