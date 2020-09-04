package smsgateway.siswa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import smsgateway.Point.PointController;
import smsgateway.pengaturan.Koneksi;

public class SiswaController {
        Connection conn = new Koneksi().getConnection();
        PreparedStatement pst;
        ResultSet rs;
        DefaultTableModel tabMode;
    
    public void refresh(SiswaView sv){
        sv.buttonSimpan.setEnabled(true);
        sv.buttonHapus.setEnabled(false);
        sv.buttonUbah.setEnabled(false);
        sv.textNis.setText("");
        sv.textNama.setText("");
        sv.textPointSisa.setText("0");
        sv.textPointAwal.setText("0");
        sv.comboKelas.setSelectedIndex(0);
        sv.radioLaki.setSelected(false);
        sv.radioPerempuan.setSelected(false);
        sv.textAlamat.setText("");
        sv.textnohp.setText("");
    }
    private String validasi(SiswaView sv){
        String ket;        
        if (sv.textNis.getText().equals("")){
            ket = "NIS belum diisi!";
        }
        else if (sv.textNama.getText().equals("")){
            ket = "Nama belum diisi!";
        }
        else if (sv.textPointSisa.getText().equals("")) {
            ket = "Point belum diisi!";
        }
        else if (sv.comboKelas.getSelectedItem().equals("")){
            ket = "Kelas belum diisi!";            
        }
        else if (sv.radioLaki.isSelected()==false&&sv.radioPerempuan.isSelected()==false){
            ket = "Jenis Kelamin belum diisi!";
        }
        else if(sv.textAlamat.getText().equals("")){
            ket = "Alamat belum diisi!";
        }
        else if (sv.textnohp.getText().equals("")) {
            ket = "No Hp belum diisi";
        }
        else{
            ket = "Sukses";
        }
        return ket;
    }
    
    
    public void simpan_siswa(SiswaView sv){
        if (validasi(sv).equals("Sukses")){
                 try {
                int isSimpan;
                String jk="";
                     if (sv.radioLaki.isSelected()==true) {
                         jk="L";
                    }else if (sv.radioPerempuan.isSelected()==true) {
                         jk="P";
                     }
                pst = conn.prepareStatement("insert into tb_siswa values(?,?,?,?,?,?,?)");                
                pst.setString(1, sv.textNis.getText());
                pst.setString(2, sv.textNama.getText());
                pst.setString(3, sv.textPointSisa.getText());
                pst.setString(4, sv.comboKelas.getSelectedItem().toString());
                pst.setString(5, jk);
                pst.setString(6, sv.textAlamat.getText());
                pst.setString(7, sv.textnohp.getText());
                isSimpan = pst.executeUpdate();
                if (isSimpan == 1){
                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan!", "Sukses Simpan Data", JOptionPane.INFORMATION_MESSAGE);
                    refresh(sv);
                    tampil_data(sv);
                }else{
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada simpan data","Gagal Simpan Data",JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses simpan data.\nDetail: "+ex.toString());
                Logger.getLogger(PointController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, validasi(sv),"Kesalahan Input Data!",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapus_siswa(SiswaView sv){
        int hapus = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data dengan NIS:"+ sv.textNis.getText() + "?",
                "Hapus Data?",JOptionPane.YES_NO_OPTION);
        if (hapus == JOptionPane.YES_OPTION){
            try {
                int isHapus;
                pst = conn.prepareStatement("delete from tb_siswa where nis=?");                
                pst.setString(1, sv.textNis.getText());
                isHapus = pst.executeUpdate();
                if (isHapus == 1){
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!","Hapus Data Sukses",JOptionPane.INFORMATION_MESSAGE);
                    refresh(sv);
                    tampil_data(sv);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada hapus data barang\nDetail: "+ex.toString());
                Logger.getLogger(PointController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void ubah_siswa(SiswaView sv){
        int ubah = JOptionPane.showConfirmDialog(null, "Yakin ingin mengubah data dengan NIS:"+ sv.textNis.getText() + "?",
                "Ubah Data?",JOptionPane.YES_NO_OPTION);
        if (ubah == JOptionPane.YES_OPTION){
            if(validasi(sv).equals("Sukses")){
                try {
                    String jk="";
                    if (sv.radioLaki.isSelected()==true) {
                        jk="L";
                    }else if (sv.radioPerempuan.isSelected()==true) {
                        jk="P";
                    }
                    int isUbah;
                    pst = conn.prepareStatement("update tb_siswa set nama_siswa=?,sisa_point=?, kelas=?, jk=?, alamat=?, no_hp=? where nis=?");                                     
                    pst.setString(1, sv.textNama.getText());
                    pst.setString(2, sv.textPointSisa.getText());
                    pst.setString(3, sv.comboKelas.getSelectedItem().toString());
                    pst.setString(4, jk);
                    pst.setString(5, sv.textAlamat.getText());
                    pst.setString(6, sv.textnohp.getText());
                    pst.setString(7, sv.textNis.getText());
                    isUbah = pst.executeUpdate();
                    if (isUbah == 1){
                        JOptionPane.showMessageDialog(null, "Data berhasil diperbaharui!","Sukses Ubah Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        refresh(sv);
                        tampil_data(sv);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses ubah data \nDetail: "+ex.toString(),
                            "Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(PointController.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }else{
                JOptionPane.showMessageDialog(null, validasi(sv),"Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
            }
        }       
    }
    
    public void tampil_data(SiswaView sv){
        Object[] Baris ={"NIS","Nama Siswa","Sisa Point","Kelas","Jenkel","Alamat","No Hp"};
        tabMode = new DefaultTableModel(null, Baris);
        sv.tabelSiswa.setModel(tabMode);   
        String sql ="select * from tb_siswa";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet hasil= pst.executeQuery(sql);
            while(hasil.next()){
                String nis = hasil.getString("nis");
                String nama = hasil.getString("nama_siswa");
                String point = hasil.getString("sisa_point");
                String kelas = hasil.getString("kelas");
                String jk = hasil.getString("jk");
                String jenkel ;
                if (jk.equals("L")) {
                    jenkel="Laki-Laki";
                }else{
                    jenkel="Perempuan";
                }
                String alamat = hasil.getString("alamat");
                String nohp = hasil.getString("no_hp");
                String[] data={nis,nama,point,kelas,jenkel,alamat,nohp};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(sv, "Terjadi kesalahan pada tampil data.\nDetails:"+e.toString(),
                    "Kesalahan Tampil Data",JOptionPane.ERROR_MESSAGE);
            System.out.println("Terjadi Kesalahan pada tampil data.\nDetails:"+e.toString());
        }
    }
    
    public void cari_siswa(SiswaView sv){
        Object[] Baris ={"NIS","Nama Siswa","Sisa Point","Kelas","Jenkel","Alamat","No Hp"};
        tabMode = new DefaultTableModel(null, Baris);
        sv.tabelSiswa.setModel(tabMode);   
        try {
        pst = conn.prepareStatement("select * from tb_siswa where nis like '%"+sv.textCari.getText()+"%'");
        rs = pst.executeQuery();
            while(rs.next()){
                String nis = rs.getString("nis");
                String nama = rs.getString("nama_siswa");
                String point = rs.getString("sisa_point");
                String kelas = rs.getString("kelas");
                String jk = rs.getString("jk");
                String jenkel;
                if (jk.equals("L")) {
                    jk="Laki-Laki";
                }else if (jk.equals("P")) {
                    jk="Perempuan";
                }
                String alamat = rs.getString("alamat");
                String nohp = rs.getString("no_hp");
                String[] data={nis,nama,point,kelas,jk,alamat,nohp};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(sv, "Terjadi kesalahan pada tampil data.\nDetails:"+e.toString(),
                    "Kesalahan Tampil Data",JOptionPane.ERROR_MESSAGE);
            System.out.println("Terjadi Kesalahan pada tampil data.\nDetails:"+e.toString());
        }
    }
    
    public void pilih_siswa (SiswaView sv){
        int row = sv.tabelSiswa.getSelectedRow();
        sv.textNis.setText(sv.tabelSiswa.getValueAt(row, 0).toString());
        sv.textNama.setText(sv.tabelSiswa.getValueAt(row, 1).toString());
        sv.textPointSisa.setText(sv.tabelSiswa.getValueAt(row, 2).toString());
        sv.comboKelas.setSelectedItem(sv.tabelSiswa.getValueAt(row, 3).toString());
        String jenkel=sv.tabelSiswa.getValueAt(row, 4).toString();
        if (jenkel.equals("Laki-Laki")) {
            sv.radioLaki.setSelected(true);
        }else{
            sv.radioPerempuan.setSelected(true);
        }
        sv.textAlamat.setText(sv.tabelSiswa.getValueAt(row, 5).toString());
        sv.textnohp.setText(sv.tabelSiswa.getValueAt(row, 6).toString());                
        sv.textNis.setEnabled(true);
        sv.buttonSimpan.setEnabled(false);
        sv.buttonUbah.setEnabled(true);
        sv.buttonHapus.setEnabled(true);
    }    
    private void ubah_point(String kode, int qty){
         try {
             int stokLama=0, stokBaru;
             pst = conn.prepareStatement("select sisa_point from tb_siswa where nis='"+kode+"'");
             rs = pst.executeQuery();
             while (rs.next()){
                 stokLama = rs.getInt(1);
             }
             stokBaru = stokLama - qty;
             pst = conn.prepareStatement("update tb_siswa set sisa_point=? where nis=?");
             pst.setInt(1, stokBaru);
             pst.setString(2, kode);
             pst.executeUpdate();
         } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada ubah stok barang: Details\n"+ex.toString(),"",JOptionPane.ERROR_MESSAGE);
             Logger.getLogger(SiswaController.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
}
