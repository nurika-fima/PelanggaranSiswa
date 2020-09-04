package smsgateway.Point;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import smsgateway.pengaturan.Koneksi;

public class PointController {
        
    Connection conn = new Koneksi().getConnection();
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel tabMode;
    
    public String kode_otomatis(){
        String kode = "";
        int kodeLama;
        try {
            pst = conn.prepareStatement("select kode_pl from tb_point order by kode_pl desc limit 1");
            rs = pst.executeQuery();
            if(!rs.next()){
                kode = "PNT-0001";
            }else{
                kodeLama = Integer.parseInt(rs.getString(1).substring(5))+1;
                if (kodeLama<10){
                    kode = "PNT-000"+kodeLama;
                }else if(kodeLama>=10 && kodeLama < 100){
                    kode = "PNT-00"+kodeLama;
                }else if(kodeLama>=100 && kodeLama < 1000){
                    kode = "PNT-0"+kodeLama;
                }else{
                    kode = "PNT-"+kodeLama;
                }
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Terjadi kesalahan pada kode otomatis. Details:\n"+ex.toString());
            Logger.getLogger(PointController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kode;
    }
    
    public void refresh(PointView pv){
        pv.textKodePelanggaran.setEnabled(false);
        pv.buttonSimpan.setEnabled(true);
        pv.buttonHapus.setEnabled(false);
        pv.buttonUbah.setEnabled(false);
        pv.textKodePelanggaran.setText(kode_otomatis());
        pv.textNamaPelanggaran.setText("");
        pv.textPointPelanggaran.setText("");
        pv.ComboPilih.setSelectedIndex(0);
    }
    private String validasi(PointView pv){
        String ket;        
        if (pv.textKodePelanggaran.getText().equals("")){
            ket = "Kode belum diisi!";
        }
        else if (pv.textNamaPelanggaran.getText().equals("")){
            ket = "Nama belum diisi!";
        }
        else if (pv.ComboPilih.getSelectedItem().equals("")){
            ket = "Kategori belum diisi!";            
        }
        else if(pv.textPointPelanggaran.getText().equals("")){
            ket = "Stok belum diisi!";
        }
        else{
            ket = "Sukses";
        }
        return ket;
    }
    
    
    public void simpan_point(PointView pv){
        //Ini adalah method simpan barang. Bertujuan untuk menyimpan data barang yang diinput ke tabel barang
        if (validasi(pv).equals("Sukses")){
                 try {
                int isSimpan;
                pst = conn.prepareStatement("insert into tb_point values(?,?,?,?)");                
                pst.setString(1, pv.textKodePelanggaran.getText()); //maksud 1 disini adalah kolom pertama dari tabel barang
                pst.setString(2, pv.textNamaPelanggaran.getText());
                pst.setString(3, pv.ComboPilih.getSelectedItem().toString());
                pst.setString(4, pv.textPointPelanggaran.getText());
                isSimpan = pst.executeUpdate();
                if (isSimpan == 1){
                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan!", "Sukses Simpan Data", JOptionPane.INFORMATION_MESSAGE);
                    refresh(pv);
                    tampil_data(pv);
                }else{
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada simpan data","Gagal Simpan Data",JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses simpan data.\nDetail: "+ex.toString());
                Logger.getLogger(PointController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, validasi(pv),"Kesalahan Input Data!",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapus_point(PointView pv){
        //ini adalah method hapus barang. Digunakan untuk menghapus data barang yang ada pada tabel barang
        int hapus = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data dengan kode pelanggaran:"+ pv.textKodePelanggaran.getText() + "?",
                "Hapus Data?",JOptionPane.YES_NO_OPTION);
        if (hapus == JOptionPane.YES_OPTION){
            try {
                int isHapus;
                pst = conn.prepareStatement("delete from tb_point where kode_pl=?");                
                pst.setString(1, pv.textKodePelanggaran.getText());
                isHapus = pst.executeUpdate();
                if (isHapus == 1){
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!","Hapus Data Sukses",JOptionPane.INFORMATION_MESSAGE);
                    refresh(pv);
                    tampil_data(pv);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada hapus data barang\nDetail: "+ex.toString());
                Logger.getLogger(PointController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void ubah_point(PointView pv){
        //ini adalah method ubah barang. Digunakan untuk mengubah data barang yang ada pada tabel barang
        int ubah = JOptionPane.showConfirmDialog(null, "Yakin ingin mengubah data dengan kode pelanggaran:"+ pv.textKodePelanggaran.getText() + "?",
                "Ubah Data?",JOptionPane.YES_NO_OPTION);
        if (ubah == JOptionPane.YES_OPTION){
            if(validasi(pv).equals("Sukses")){
                try {
                    int isUbah;
                   // pst = conn.prepareStatement("update tbl_barang set nama_barang=?, kategori=?, harga=?, stok=? where kode_barang=?");                    
                    pst = conn.prepareStatement("update tb_point set nama_pl=?, kategori=?, point=? where kode_pl=?");                                     
                    pst.setString(1, pv.textNamaPelanggaran.getText());
                    pst.setString(2, pv.ComboPilih.getSelectedItem().toString());
                    pst.setString(3, pv.textPointPelanggaran.getText());
                    pst.setString(4, pv.textKodePelanggaran.getText());
                    isUbah = pst.executeUpdate();
                    if (isUbah == 1){
                        JOptionPane.showMessageDialog(null, "Data berhasil diperbaharui!","Sukses Ubah Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        refresh(pv);
                        tampil_data(pv);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses ubah data \nDetail: "+ex.toString(),
                            "Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(PointController.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }else{
                JOptionPane.showMessageDialog(null, validasi(pv),"Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
            }
        }       
    }
    
    public void tampil_data(PointView pv){
        Object[] Baris ={"Kode Pelanggaran","Nama Pelangggaran","Kategori","Point"};
        tabMode = new DefaultTableModel(null, Baris);
        pv.tabelPelanggaran.setModel(tabMode);  
        String sql = "select * from tb_point";
        try {
           PreparedStatement pst = conn.prepareStatement(sql);
           ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String kode = rs.getString("kode_pl"); //rs.getString(1);
                String nama = rs.getString("nama_pl");
                String kategori = rs.getString("kategori");
                String point = rs.getString("point");
                String[] data={kode,nama,kategori, point};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan pada tampil data.\nDetails: "+e.toString());
        }
    }
    
    public void cari_point(PointView pv){
        Object[] Baris ={"Kode Pelanggaran","Nama Pelanggaran","Kategori","Point"};
        tabMode = new DefaultTableModel(null, Baris);
        pv.tabelPelanggaran.setModel(tabMode);  
        try {
            pst = conn.prepareStatement("select*from tb_point where point like '%"+pv.textCari.getText()+"%'");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                String kode = rs.getString("kode_pl");
                String nama = rs.getString("nama_pl");
                String kategori = rs.getString("kategori");
                String point = rs.getString("point");
                String[] data={kode,nama,kategori,point};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pv, "Terjadi kesalahan pada cari data pelanggan.\nDetails:"+e.toString(),
                    "Kesalahan Cari Data", JOptionPane.ERROR_MESSAGE);
            System.out.println("Terjadi kesalahan pada tampil data pelanggan.\nDetails: "+e.toString());
        }
    }
    
    public void pilih_point(PointView pv){
        //ini adalah method pilih barang. Digunakan untuk menampilkan data barang yang dipilih pada tblBarang ke inputan
        int row = pv.tabelPelanggaran.getSelectedRow();
        pv.textKodePelanggaran.setText(pv.tabelPelanggaran.getValueAt(row, 0).toString());
        pv.textNamaPelanggaran.setText(pv.tabelPelanggaran.getValueAt(row, 1).toString());
        pv.ComboPilih.setSelectedItem(pv.tabelPelanggaran.getValueAt(row, 2).toString());
        pv.textPointPelanggaran.setText(pv.tabelPelanggaran.getValueAt(row, 3).toString());                
        pv.textKodePelanggaran.setEnabled(false);
        pv.buttonSimpan.setEnabled(false);
        pv.buttonUbah.setEnabled(true);
        pv.buttonHapus.setEnabled(true);
    }        

}