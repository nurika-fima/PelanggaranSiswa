package smsgateway.penanggung_jawab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import smsgateway.Point.PointController;
import smsgateway.Point.PointView;
import smsgateway.pengaturan.Koneksi;

    public class PenanggungJawabController {
        Connection conn = new Koneksi().getConnection();
        PreparedStatement pst;
        ResultSet rs;
        DefaultTableModel tabMode;
    
    public void refresh(PenangggungJawabView pjv){
        pjv.buttonSimpan.setEnabled(true);
        pjv.buttonHapus.setEnabled(false);
        pjv.buttonUbah.setEnabled(false);
        pjv.textNIP.setText("");
        pjv.textNama.setText("");
        pjv.comboPilih.setSelectedIndex(0);
        pjv.radioLaki.setSelected(false);
        pjv.radioCewe.setSelected(false);
        pjv.textNoHp.setText("");
    }
    private String validasi(PenangggungJawabView pv){
        String ket;        
        if (pv.textNIP.getText().equals("")){
            ket = "NIP belum diisi!";
        }
        else if (pv.textNama.getText().equals("")){
            ket = "Nama belum diisi!";
        }
        else if (pv.comboPilih.getSelectedItem().equals("")){
            ket = "Jabatan belum diisi!";            
        }
        else if (pv.radioLaki.isSelected()==false&&pv.radioCewe.isSelected()==false){
            ket = "Jenis Kelamin belum diisi!";
        }
        else if(pv.textNoHp.getText().equals("")){
            ket = "No Hp belum diisi!";
        }
        else{
            ket = "Sukses";
        }
        return ket;
    }
    
    
    public void simpan_pj(PenangggungJawabView pv){
        if (validasi(pv).equals("Sukses")){
                 try {
                int isSimpan;
                String jk="";
                     if (pv.radioLaki.isSelected()==true) {
                         jk="L";
                    }else if (pv.radioCewe.isSelected()==true) {
                         jk="W";
                     }
                pst = conn.prepareStatement("insert into tb_penanggungjawab values(?,?,?,?,?)");                
                pst.setString(1, pv.textNIP.getText());
                pst.setString(2, pv.textNama.getText());
                pst.setString(3, pv.comboPilih.getSelectedItem().toString());
                pst.setString(4, jk);
                pst.setString(5, pv.textNoHp.getText());
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
    
    public void hapus_pj(PenangggungJawabView pv){
        int hapus = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data dengan NIP:"+ pv.textNIP.getText() + "?",
                "Hapus Data?",JOptionPane.YES_NO_OPTION);
        if (hapus == JOptionPane.YES_OPTION){
            try {
                int isHapus;
                pst = conn.prepareStatement("delete from tb_penanggungjawab where nip=?");                
                pst.setString(1, pv.textNIP.getText());
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
    
    public void ubah_pj(PenangggungJawabView pv){
        int ubah = JOptionPane.showConfirmDialog(null, "Yakin ingin mengubah data dengan NIP:"+ pv.textNIP.getText() + "?",
                "Ubah Data?",JOptionPane.YES_NO_OPTION);
        if (ubah == JOptionPane.YES_OPTION){
            if(validasi(pv).equals("Sukses")){
                try {
                    String jk="";
                    if (pv.radioLaki.isSelected()==true) {
                        jk="L";
                    }else if (pv.radioCewe.isSelected()==true) {
                        jk="P";
                    }
                    int isUbah;
                    pst = conn.prepareStatement("update tb_penanggungjawab set nama_pj=?, jabatan=?, jk=?, no_hp=? where nip=?");                                     
                    pst.setString(1, pv.textNama.getText());
                    pst.setString(2, pv.comboPilih.getSelectedItem().toString());
                    pst.setString(3, jk);
                    pst.setString(4, pv.textNoHp.getText());
                    pst.setString(5, pv.textNIP.getText());
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
    
public void tampil_data(PenangggungJawabView pjv){
        Object[] Baris ={"NIP","Nama Penanggung Jawab","Jabatan","Jenkel","No Hp"};
        tabMode = new DefaultTableModel(null, Baris);
        pjv.tabelCari.setModel(tabMode);   
        String sql ="select * from tb_penanggungjawab";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet hasil= pst.executeQuery(sql);
            while(hasil.next()){
                String nis = hasil.getString("nip");
                String nama = hasil.getString("nama_pj");
                String jabatan= hasil.getString("jabatan");
                String jk = hasil.getString("jk");
                String jenkel ;
                if (jk.equals("L")) {
                    jenkel="Laki-Laki";
                }else{
                    jenkel="Perempuan";
                }
                String nohp = hasil.getString("no_hp");
                String[] data={nis,nama,jabatan,jenkel,nohp};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pjv, "Terjadi kesalahan pada tampil data.\nDetails:"+e.toString(),
                    "Kesalahan Tampil Data",JOptionPane.ERROR_MESSAGE);
            System.out.println("Terjadi Kesalahan pada tampil data.\nDetails:"+e.toString());
        }
    }
    
    public void cari_pj(PenangggungJawabView pjv){
        Object[] Baris ={"NIP","Nama Penanggung Jawab","Jabatan","Jenkel","No Hp"};
        tabMode = new DefaultTableModel(null, Baris);
        pjv.tabelCari.setModel(tabMode);   
        try {
        pst = conn.prepareStatement("select * from tb_penanggungjawab where nip like '%"+pjv.textCari.getText()+"%'");
        rs = pst.executeQuery();
            while(rs.next()){
                String nis = rs.getString("nip");
                String nama = rs.getString("nama_pj");
                String jabatan = rs.getString("jabatan");
                String jk = rs.getString("jk");
                String jenkel;
                if (jk.equals("L")) {
                    jk="Laki-Laki";
                }else if (jk.equals("P")) {
                    jk="Perempuan";
                }
                String nohp = rs.getString("no_hp");
                String[] data={nis,nama,jabatan,jk,nohp};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pjv, "Terjadi kesalahan pada tampil data.\nDetails:"+e.toString(),
                    "Kesalahan Tampil Data",JOptionPane.ERROR_MESSAGE);
            System.out.println("Terjadi Kesalahan pada tampil data.\nDetails:"+e.toString());
        }
    }
    
    public void pilih_pj(PenangggungJawabView pv){
        int row = pv.tabelCari.getSelectedRow();
        pv.textNIP.setText(pv.tabelCari.getValueAt(row, 0).toString());
        pv.textNama.setText(pv.tabelCari.getValueAt(row, 1).toString());
        pv.comboPilih.setSelectedItem(pv.tabelCari.getValueAt(row, 2).toString());
        String jenkel=pv.tabelCari.getValueAt(row, 3).toString();
        if (jenkel.equals("Laki-Laki")) {
            pv.radioLaki.setSelected(true);
        }else{
            pv.radioCewe.setSelected(true);
        }
        pv.textNoHp.setText(pv.tabelCari.getValueAt(row, 4).toString());                
        pv.textNIP.setEnabled(true);
        pv.buttonSimpan.setEnabled(false);
        pv.buttonUbah.setEnabled(true);
        pv.buttonHapus.setEnabled(true);
    }    
}
