/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok5;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "buku")
@NamedQueries({
    @NamedQuery(name = "Buku.findAll", query = "SELECT b FROM Buku b"),
    @NamedQuery(name = "Buku.findByKodeBuku", query = "SELECT b FROM Buku b WHERE b.kodeBuku = :kodeBuku"),
    @NamedQuery(name = "Buku.findByJudulBuku", query = "SELECT b FROM Buku b WHERE b.judulBuku = :judulBuku"),
    @NamedQuery(name = "Buku.findByTahunTerbit", query = "SELECT b FROM Buku b WHERE b.tahunTerbit = :tahunTerbit"),
    @NamedQuery(name = "Buku.findByRakBuku", query = "SELECT b FROM Buku b WHERE b.rakBuku = :rakBuku"),
    @NamedQuery(name = "Buku.findByHargaBuku", query = "SELECT b FROM Buku b WHERE b.hargaBuku = :hargaBuku"),
    @NamedQuery(name = "Buku.findByPenulis", query = "SELECT b FROM Buku b WHERE b.penulis = :penulis")})
public class Buku implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "kode_buku")
    private String kodeBuku;
    @Basic(optional = false)
    @Column(name = "judul_buku")
    private String judulBuku;
    @Basic(optional = false)
    @Column(name = "tahun_terbit")
    private String tahunTerbit;
    @Basic(optional = false)
    @Column(name = "rak_buku")
    private String rakBuku;
    @Basic(optional = false)
    @Column(name = "harga_buku")
    private String hargaBuku;
    @Basic(optional = false)
    @Column(name = "penulis")
    private String penulis;
    @JoinColumn(name = "id_toko", referencedColumnName = "id_toko")
    @ManyToOne(optional = false)
    private TokoBuku idToko;
    @JoinColumn(name = "id_supplier", referencedColumnName = "id_supplier")
    @ManyToOne(optional = false)
    private Supplier idSupplier;

    public Buku() {
    }

    public Buku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }

    public Buku(String kodeBuku, String judulBuku, String tahunTerbit, String rakBuku, String hargaBuku, String penulis) {
        this.kodeBuku = kodeBuku;
        this.judulBuku = judulBuku;
        this.tahunTerbit = tahunTerbit;
        this.rakBuku = rakBuku;
        this.hargaBuku = hargaBuku;
        this.penulis = penulis;
    }

    public String getKodeBuku() {
        return kodeBuku;
    }

    public void setKodeBuku(String kodeBuku) {
        this.kodeBuku = kodeBuku;
    }

    public String getJudulBuku() {
        return judulBuku;
    }

    public void setJudulBuku(String judulBuku) {
        this.judulBuku = judulBuku;
    }

    public String getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(String tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public String getRakBuku() {
        return rakBuku;
    }

    public void setRakBuku(String rakBuku) {
        this.rakBuku = rakBuku;
    }

    public String getHargaBuku() {
        return hargaBuku;
    }

    public void setHargaBuku(String hargaBuku) {
        this.hargaBuku = hargaBuku;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public TokoBuku getIdToko() {
        return idToko;
    }

    public void setIdToko(TokoBuku idToko) {
        this.idToko = idToko;
    }

    public Supplier getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Supplier idSupplier) {
        this.idSupplier = idSupplier;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodeBuku != null ? kodeBuku.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Buku)) {
            return false;
        }
        Buku other = (Buku) object;
        if ((this.kodeBuku == null && other.kodeBuku != null) || (this.kodeBuku != null && !this.kodeBuku.equals(other.kodeBuku))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.kelompok5.Buku[ kodeBuku=" + kodeBuku + " ]";
    }
    
}
