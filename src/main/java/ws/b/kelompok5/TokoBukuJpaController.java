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
public class TokoBukuJpaController implements Serializable {

    public TokoBukuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TokoBuku tokoBuku) throws PreexistingEntityException, Exception {
        if (tokoBuku.getBukuCollection() == null) {
            tokoBuku.setBukuCollection(new ArrayList<Buku>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Buku> attachedBukuCollection = new ArrayList<Buku>();
            for (Buku bukuCollectionBukuToAttach : tokoBuku.getBukuCollection()) {
                bukuCollectionBukuToAttach = em.getReference(bukuCollectionBukuToAttach.getClass(), bukuCollectionBukuToAttach.getKodeBuku());
                attachedBukuCollection.add(bukuCollectionBukuToAttach);
            }
            tokoBuku.setBukuCollection(attachedBukuCollection);
            em.persist(tokoBuku);
            for (Buku bukuCollectionBuku : tokoBuku.getBukuCollection()) {
                TokoBuku oldIdTokoOfBukuCollectionBuku = bukuCollectionBuku.getIdToko();
                bukuCollectionBuku.setIdToko(tokoBuku);
                bukuCollectionBuku = em.merge(bukuCollectionBuku);
                if (oldIdTokoOfBukuCollectionBuku != null) {
                    oldIdTokoOfBukuCollectionBuku.getBukuCollection().remove(bukuCollectionBuku);
                    oldIdTokoOfBukuCollectionBuku = em.merge(oldIdTokoOfBukuCollectionBuku);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTokoBuku(tokoBuku.getIdToko()) != null) {
                throw new PreexistingEntityException("TokoBuku " + tokoBuku + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TokoBuku tokoBuku) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TokoBuku persistentTokoBuku = em.find(TokoBuku.class, tokoBuku.getIdToko());
            Collection<Buku> bukuCollectionOld = persistentTokoBuku.getBukuCollection();
            Collection<Buku> bukuCollectionNew = tokoBuku.getBukuCollection();
            List<String> illegalOrphanMessages = null;
            for (Buku bukuCollectionOldBuku : bukuCollectionOld) {
                if (!bukuCollectionNew.contains(bukuCollectionOldBuku)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Buku " + bukuCollectionOldBuku + " since its idToko field is not nullable.");
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
            tokoBuku.setBukuCollection(bukuCollectionNew);
            tokoBuku = em.merge(tokoBuku);
            for (Buku bukuCollectionNewBuku : bukuCollectionNew) {
                if (!bukuCollectionOld.contains(bukuCollectionNewBuku)) {
                    TokoBuku oldIdTokoOfBukuCollectionNewBuku = bukuCollectionNewBuku.getIdToko();
                    bukuCollectionNewBuku.setIdToko(tokoBuku);
                    bukuCollectionNewBuku = em.merge(bukuCollectionNewBuku);
                    if (oldIdTokoOfBukuCollectionNewBuku != null && !oldIdTokoOfBukuCollectionNewBuku.equals(tokoBuku)) {
                        oldIdTokoOfBukuCollectionNewBuku.getBukuCollection().remove(bukuCollectionNewBuku);
                        oldIdTokoOfBukuCollectionNewBuku = em.merge(oldIdTokoOfBukuCollectionNewBuku);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tokoBuku.getIdToko();
                if (findTokoBuku(id) == null) {
                    throw new NonexistentEntityException("The tokoBuku with id " + id + " no longer exists.");
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
            TokoBuku tokoBuku;
            try {
                tokoBuku = em.getReference(TokoBuku.class, id);
                tokoBuku.getIdToko();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tokoBuku with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Buku> bukuCollectionOrphanCheck = tokoBuku.getBukuCollection();
            for (Buku bukuCollectionOrphanCheckBuku : bukuCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TokoBuku (" + tokoBuku + ") cannot be destroyed since the Buku " + bukuCollectionOrphanCheckBuku + " in its bukuCollection field has a non-nullable idToko field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tokoBuku);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TokoBuku> findTokoBukuEntities() {
        return findTokoBukuEntities(true, -1, -1);
    }

    public List<TokoBuku> findTokoBukuEntities(int maxResults, int firstResult) {
        return findTokoBukuEntities(false, maxResults, firstResult);
    }

    private List<TokoBuku> findTokoBukuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TokoBuku.class));
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

    public TokoBuku findTokoBuku(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TokoBuku.class, id);
        } finally {
            em.close();
        }
    }

    public int getTokoBukuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TokoBuku> rt = cq.from(TokoBuku.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
