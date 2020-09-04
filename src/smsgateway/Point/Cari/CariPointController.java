/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smsgateway.Point.Cari;

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
public class CariPointController {
    Connection conn = new Koneksi().getConnection();
    PreparedStatement pst;
    ResultSet hasil;
    DefaultTableModel tabMode;   
    
    public void tampil_data(CariPointView pv){
        //ini adalah method tampil data. Digunakan untuk menampilkan data pelanggan yang ada pada tabel pelanggan
        Object[] Baris ={"Kode Pelanggaran","Nama Pelanggaran","Kategori","Point"};
        tabMode = new DefaultTableModel(null, Baris);
        pv.tabelPoint.setModel(tabMode);    
        try {
            if (pv.textCari.getText().equals("")) {
                String sql = "select*from tb_point";
                 pst  = conn.prepareStatement(sql);      
                }
           ResultSet hasil = pst.executeQuery();
            while(hasil.next()){
                String kode = hasil.getString("kode_pelanggaran"); //hasil.getString(1);
                String nama = hasil.getString("nama_pelanggaran");
                String kategori = hasil.getString("kategori");
                String point = hasil.getString("point");
                String[] data={kode,nama,kategori,point};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pv,"Terjadi kesalahan pada tampil data.\nDetails: "+e.toString(),
                    "Kesalahan Tampil Data", JOptionPane.ERROR_MESSAGE);
            System.err.println ("Terjadi kesalahan pada tampil data.\nDetails: "+e.toString());
        }
    }
    
        public void pilih_point(CariPointView pv){
        //ini adalah method pilih pelanggan. Digunakan untuk menampilkan data pelanggan yang dipilih pada tblBarang ke inputan
        int row = pv.tabelPoint.getSelectedRow();
        PelanggaranView.textKodePelanggaran.setText(pv.tabelPoint.getValueAt(row, 0).toString());
        PelanggaranView.textNamaPelanggaran.setText(pv.tabelPoint.getValueAt(row, 1).toString());
        PelanggaranView.textKategori.setText(pv.tabelPoint.getValueAt(row, 2).toString());
        PelanggaranView.textPointPelanggaran.setText(pv.tabelPoint.getValueAt(row, 3).toString());
        pv.dispose();
    }  
}
