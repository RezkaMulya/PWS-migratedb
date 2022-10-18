/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok5;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "supplier")
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findByIdSupplier", query = "SELECT s FROM Supplier s WHERE s.idSupplier = :idSupplier"),
    @NamedQuery(name = "Supplier.findByNamaSupplier", query = "SELECT s FROM Supplier s WHERE s.namaSupplier = :namaSupplier"),
    @NamedQuery(name = "Supplier.findByAlamatSupplier", query = "SELECT s FROM Supplier s WHERE s.alamatSupplier = :alamatSupplier"),
    @NamedQuery(name = "Supplier.findByTelpSupplier", query = "SELECT s FROM Supplier s WHERE s.telpSupplier = :telpSupplier")})
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_supplier")
    private String idSupplier;
    @Basic(optional = false)
    @Column(name = "nama_supplier")
    private String namaSupplier;
    @Basic(optional = false)
    @Column(name = "alamat_supplier")
    private String alamatSupplier;
    @Basic(optional = false)
    @Column(name = "telp_supplier")
    private String telpSupplier;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSupplier")
    private Collection<Buku> bukuCollection;

    public Supplier() {
    }

    public Supplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public Supplier(String idSupplier, String namaSupplier, String alamatSupplier, String telpSupplier) {
        this.idSupplier = idSupplier;
        this.namaSupplier = namaSupplier;
        this.alamatSupplier = alamatSupplier;
        this.telpSupplier = telpSupplier;
    }

    public String getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public void setNamaSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
    }

    public String getAlamatSupplier() {
        return alamatSupplier;
    }

    public void setAlamatSupplier(String alamatSupplier) {
        this.alamatSupplier = alamatSupplier;
    }

    public String getTelpSupplier() {
        return telpSupplier;
    }

    public void setTelpSupplier(String telpSupplier) {
        this.telpSupplier = telpSupplier;
    }

    public Collection<Buku> getBukuCollection() {
        return bukuCollection;
    }

    public void setBukuCollection(Collection<Buku> bukuCollection) {
        this.bukuCollection = bukuCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSupplier != null ? idSupplier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.idSupplier == null && other.idSupplier != null) || (this.idSupplier != null && !this.idSupplier.equals(other.idSupplier))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.kelompok5.Supplier[ idSupplier=" + idSupplier + " ]";
    }
    
}
