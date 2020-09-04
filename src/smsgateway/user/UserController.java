package smsgateway.user;

import com.sun.istack.internal.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import smsgateway.pengaturan.Koneksi;

public class UserController {
    Connection conn = new Koneksi().getConnection();
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel tableModel;
    
    public void refresh(UserView uv){
        uv.buttonSimpan.setEnabled(true);
        uv.buttonHapus.setEnabled(false);
        uv.buttonUbah.setEnabled(false);
        uv.textNama.setText("");
        uv.textUsern.setText("");
        uv.textPass.setText("");
    }
    
    private String validasi(UserView uv){
        String ket;
        if (uv.textNama.getText().equals("")) {
            ket = "Nama belum terisi";
        }
        else if (uv.textUsern.getText().equals("")) {
            ket = "Username belum terisi";
        }
        else if (uv.textPass.getText().equals("")){
            ket = "Password belum terisi";
        }
        else{
            ket = "Sukses";
        }
        return ket;
    }
    
    public void tampil_data(UserView uv){
        Object[] Baris={"Nama User","User Name","Password"};
        tableModel=new DefaultTableModel(null, Baris);
        uv.tabelUser.setModel(tableModel);
        try {
            pst=conn.prepareStatement("select * from tb_pengguna");
            rs=pst.executeQuery();
            while (rs.next()) {
                String namauser = rs.getString("namauser");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String[] data={namauser,username,password};
                tableModel.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan pada tampil data !\nDetails:"+e.toString());
        }
    }
    
    public void simpan_user(UserView uv){
        if (validasi(uv).equals("Sukses")) {
            try {
                int isSimpan;
                pst = conn.prepareStatement("insert into tb_pengguna values(?,?,?)");
                pst.setString(1,uv.textNama.getText());
                pst.setString(2,uv.textUsern.getText());
                pst.setString(3,uv.textPass.getText());
                isSimpan = pst.executeUpdate();
                if (isSimpan==1) {
                    JOptionPane.showMessageDialog(null,"Data berhasil disimpan!","sukses simpan data",JOptionPane.INFORMATION_MESSAGE);
                    refresh(uv);
                    tampil_data(uv);
                }else{
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada simpan data","Gagal Simpan Data",JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses simpan data!\nDetails"+ex.toString());
                java.util.logging.Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, validasi(uv),"Keselahan Input Data",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapus_user(UserView uv){
        int hapus = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data ini?"+ uv.textNama.getText() + "?,?",
                "Hapus Data?",JOptionPane.YES_NO_OPTION);
        if (hapus==JOptionPane.YES_OPTION) {
            try {
                int isHapus;
                pst=conn.prepareStatement("delete from tb_pengguna where namauser=?");
                pst.setString(1,uv.textNama.getText());
                isHapus=pst.executeUpdate();
                if (isHapus==1) {
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus","hapus data sukses",JOptionPane.INFORMATION_MESSAGE);
                    refresh(uv);
                    tampil_data(uv);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada hapus data\nDetails:"+ex.toString());
                java.util.logging.Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void ubah_user(UserView uv){
        int ubah = JOptionPane.showConfirmDialog(null, "Yakin ingin mengubah data ini:"+ uv.textNama.getText() + "?",
                "Ubah Data?",JOptionPane.YES_NO_OPTION);
        if (ubah == JOptionPane.YES_OPTION){
            if(validasi(uv).equals("Sukses")){
                try {
                    int isUbah;
                    pst = conn.prepareStatement("update tb_pengguna set username=?, password=? where namauser=?");                                     
                    pst.setString(1, uv.textUsern.getText());
                    pst.setString(2, uv.textPass.getText());
                    pst.setString(3, uv.textNama.getText());
                    isUbah = pst.executeUpdate();
                    if (isUbah == 1){
                        JOptionPane.showMessageDialog(null, "Data berhasil diperbaharui!","Sukses Ubah Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        refresh(uv);
                        tampil_data(uv);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses ubah data ini\nDetail: "+ex.toString(),
                            "Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
                    java.util.logging.Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }else{
                JOptionPane.showMessageDialog(null, validasi(uv),"Kesalahan Ubah Data",JOptionPane.ERROR_MESSAGE);
            }
        }       
    }
    public void pilih_user (UserView uv){
        //ini adalah method pilih barang. Digunakan untuk menampilkan data barang yang dipilih pada tblBarang ke inputan
        int row = uv.tabelUser.getSelectedRow();
        uv.textNama.setText(uv.tabelUser.getValueAt(row, 0).toString());
        uv.textUsern.setText(uv.tabelUser.getValueAt(row, 1).toString());               
        uv.setEnabled(false);
        uv.buttonSimpan.setEnabled(false);
        uv.buttonUbah.setEnabled(true);
        uv.buttonHapus.setEnabled(true);
    }        
    public void cari_user(UserView uv){
        Object[] Baris ={"namauser","username","password"};
        tableModel= new DefaultTableModel(null, Baris);
        uv.tabelUser.setModel(tableModel);        
        try {
            pst = conn.prepareStatement("select * from tb_pengguna where namauser like '%"+uv.textCari.getText()+"%'");
            rs = pst.executeQuery();
            while(rs.next()){
                String namauser = rs.getString("namauser");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String[] data={namauser,username,password};
                tableModel.addRow(data);
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan pada tampil data barang.\nDetails: "+e.toString());
        }
    }
}
