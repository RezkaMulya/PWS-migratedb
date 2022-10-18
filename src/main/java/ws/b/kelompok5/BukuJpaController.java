/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok5;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ws.b.kelompok5.exceptions.NonexistentEntityException;
import ws.b.kelompok5.exceptions.PreexistingEntityException;

/**
 *
 * @author user
 */
public class BukuJpaController implements Serializable {

    public BukuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Buku buku) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TokoBuku idToko = buku.getIdToko();
            if (idToko != null) {
                idToko = em.getReference(idToko.getClass(), idToko.getIdToko());
                buku.setIdToko(idToko);
            }
            Supplier idSupplier = buku.getIdSupplier();
            if (idSupplier != null) {
                idSupplier = em.getReference(idSupplier.getClass(), idSupplier.getIdSupplier());
                buku.setIdSupplier(idSupplier);
            }
            em.persist(buku);
            if (idToko != null) {
                idToko.getBukuCollection().add(buku);
                idToko = em.merge(idToko);
            }
            if (idSupplier != null) {
                idSupplier.getBukuCollection().add(buku);
                idSupplier = em.merge(idSupplier);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBuku(buku.getKodeBuku()) != null) {
                throw new PreexistingEntityException("Buku " + buku + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Buku buku) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Buku persistentBuku = em.find(Buku.class, buku.getKodeBuku());
            TokoBuku idTokoOld = persistentBuku.getIdToko();
            TokoBuku idTokoNew = buku.getIdToko();
            Supplier idSupplierOld = persistentBuku.getIdSupplier();
            Supplier idSupplierNew = buku.getIdSupplier();
            if (idTokoNew != null) {
                idTokoNew = em.getReference(idTokoNew.getClass(), idTokoNew.getIdToko());
                buku.setIdToko(idTokoNew);
            }
            if (idSupplierNew != null) {
                idSupplierNew = em.getReference(idSupplierNew.getClass(), idSupplierNew.getIdSupplier());
                buku.setIdSupplier(idSupplierNew);
            }
            buku = em.merge(buku);
            if (idTokoOld != null && !idTokoOld.equals(idTokoNew)) {
                idTokoOld.getBukuCollection().remove(buku);
                idTokoOld = em.merge(idTokoOld);
            }
            if (idTokoNew != null && !idTokoNew.equals(idTokoOld)) {
                idTokoNew.getBukuCollection().add(buku);
                idTokoNew = em.merge(idTokoNew);
            }
            if (idSupplierOld != null && !idSupplierOld.equals(idSupplierNew)) {
                idSupplierOld.getBukuCollection().remove(buku);
                idSupplierOld = em.merge(idSupplierOld);
            }
            if (idSupplierNew != null && !idSupplierNew.equals(idSupplierOld)) {
                idSupplierNew.getBukuCollection().add(buku);
                idSupplierNew = em.merge(idSupplierNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = buku.getKodeBuku();
                if (findBuku(id) == null) {
                    throw new NonexistentEntityException("The buku with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Buku buku;
            try {
                buku = em.getReference(Buku.class, id);
                buku.getKodeBuku();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The buku with id " + id + " no longer exists.", enfe);
            }
            TokoBuku idToko = buku.getIdToko();
            if (idToko != null) {
                idToko.getBukuCollection().remove(buku);
                idToko = em.merge(idToko);
            }
            Supplier idSupplier = buku.getIdSupplier();
            if (idSupplier != null) {
                idSupplier.getBukuCollection().remove(buku);
                idSupplier = em.merge(idSupplier);
            }
            em.remove(buku);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Buku> findBukuEntities() {
        return findBukuEntities(true, -1, -1);
    }

    public List<Buku> findBukuEntities(int maxResults, int firstResult) {
        return findBukuEntities(false, maxResults, firstResult);
    }

    private List<Buku> findBukuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Buku.class));
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

    public Buku findBuku(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Buku.class, id);
        } finally {
            em.close();
        }
    }

    public int getBukuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Buku> rt = cq.from(Buku.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
