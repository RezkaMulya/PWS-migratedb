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
@Table(name = "toko_buku")
@NamedQueries({
    @NamedQuery(name = "TokoBuku.findAll", query = "SELECT t FROM TokoBuku t"),
    @NamedQuery(name = "TokoBuku.findByIdToko", query = "SELECT t FROM TokoBuku t WHERE t.idToko = :idToko"),
    @NamedQuery(name = "TokoBuku.findByAlamatToko", query = "SELECT t FROM TokoBuku t WHERE t.alamatToko = :alamatToko"),
    @NamedQuery(name = "TokoBuku.findByNamaToko", query = "SELECT t FROM TokoBuku t WHERE t.namaToko = :namaToko"),
    @NamedQuery(name = "TokoBuku.findByIdPegawai", query = "SELECT t FROM TokoBuku t WHERE t.idPegawai = :idPegawai"),
    @NamedQuery(name = "TokoBuku.findByNamaPegawai", query = "SELECT t FROM TokoBuku t WHERE t.namaPegawai = :namaPegawai"),
    @NamedQuery(name = "TokoBuku.findByTelpToko", query = "SELECT t FROM TokoBuku t WHERE t.telpToko = :telpToko")})
public class TokoBuku implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_toko")
    private String idToko;
    @Basic(optional = false)
    @Column(name = "alamat_toko")
    private String alamatToko;
    @Basic(optional = false)
    @Column(name = "nama_toko")
    private String namaToko;
    @Basic(optional = false)
    @Column(name = "id_pegawai")
    private String idPegawai;
    @Basic(optional = false)
    @Column(name = "nama_pegawai")
    private String namaPegawai;
    @Basic(optional = false)
    @Column(name = "telp_toko")
    private String telpToko;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idToko")
    private Collection<Buku> bukuCollection;

    public TokoBuku() {
    }

    public TokoBuku(String idToko) {
        this.idToko = idToko;
    }

    public TokoBuku(String idToko, String alamatToko, String namaToko, String idPegawai, String namaPegawai, String telpToko) {
        this.idToko = idToko;
        this.alamatToko = alamatToko;
        this.namaToko = namaToko;
        this.idPegawai = idPegawai;
        this.namaPegawai = namaPegawai;
        this.telpToko = telpToko;
    }

    public String getIdToko() {
        return idToko;
    }

    public void setIdToko(String idToko) {
        this.idToko = idToko;
    }

    public String getAlamatToko() {
        return alamatToko;
    }

    public void setAlamatToko(String alamatToko) {
        this.alamatToko = alamatToko;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public String getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(String idPegawai) {
        this.idPegawai = idPegawai;
    }

    public String getNamaPegawai() {
        return namaPegawai;
    }

    public void setNamaPegawai(String namaPegawai) {
        this.namaPegawai = namaPegawai;
    }

    public String getTelpToko() {
        return telpToko;
    }

    public void setTelpToko(String telpToko) {
        this.telpToko = telpToko;
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
        hash += (idToko != null ? idToko.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TokoBuku)) {
            return false;
        }
        TokoBuku other = (TokoBuku) object;
        if ((this.idToko == null && other.idToko != null) || (this.idToko != null && !this.idToko.equals(other.idToko))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.kelompok5.TokoBuku[ idToko=" + idToko + " ]";
    }
    
}
