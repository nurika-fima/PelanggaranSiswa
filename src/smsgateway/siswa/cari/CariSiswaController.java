/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smsgateway.siswa.cari;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import smsgateway.Pelanggaran.PelanggaranView;
import smsgateway.pengaturan.Koneksi;

/**
 *
 * @author NurikaFima_Pahmawati
 */
public class CariSiswaController {
    Connection conn = new Koneksi().getConnection();
    PreparedStatement pst;
    ResultSet hasil;
    DefaultTableModel tabMode;   
    
    public void tampil_data(CariSiswaView pv){
        //ini adalah method tampil data. Digunakan untuk menampilkan data pelanggan yang ada pada tabel pelanggan
        Object[] Baris ={"NIS","Nama Siswa","Sisa Point","Awal Point","Kelas","Jenkel","Alamat","No Hp"};
        tabMode = new DefaultTableModel(null, Baris);
        pv.tabelSiswa.setModel(tabMode);    
        try {
            if (pv.textCari.getText().equals("")) {
                String sql = "select*from tb_siswa";
                 pst  = conn.prepareStatement(sql);      
                }
           ResultSet hasil = pst.executeQuery();
            while(hasil.next()){
                String nis = hasil.getString("nis"); //hasil.getString(1);
                String nama = hasil.getString("nama_siswa");
                String sisa_point = hasil.getString("sisa_point");
                String awal_point = hasil.getString("point_awal");
                String kelas = hasil.getString("kelas");
                String jk = hasil.getString("jk");
                String jenkel;
                    if (jk.equals("P")) {
                        jenkel = "Pria";                  
                } else{
                        jenkel = "Wanita";
                    }
                String alamat = hasil.getString("alamat");
                String noHp = hasil.getString("no_hp");
                String[] data={nis,nama,sisa_point,awal_point,kelas,jenkel, alamat, noHp};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pv,"Terjadi kesalahan pada tampil data.\nDetails: "+e.toString(),
                    "Kesalahan Tampil Data", JOptionPane.ERROR_MESSAGE);
            System.err.println ("Terjadi kesalahan pada tampil data.\nDetails: "+e.toString());
        }
    }
    
        public void pilih_siswa(CariSiswaView pv){
        //ini adalah method pilih pelanggan. Digunakan untuk menampilkan data pelanggan yang dipilih pada tblBarang ke inputan
        int row = pv.tabelSiswa.getSelectedRow();
            PelanggaranView.textNisSiswa.setText(pv.tabelSiswa.getValueAt(row, 0).toString());
        PelanggaranView.textNamaSiswa.setText(pv.tabelSiswa.getValueAt(row, 1).toString());
        PelanggaranView.textSisaPoint.setText(pv.tabelSiswa.getValueAt(row, 2).toString());
        PelanggaranView.textSisaPointPelanggaran.setText(pv.tabelSiswa.getValueAt(row, 2).toString());
        pv.dispose();
    }  
}
