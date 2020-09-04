/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smsgateway.siswa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import smsgateway.pengaturan.Koneksi;

/**
 *
 * @author NurikaFima_Pahmawati
 */
public class ReportController {
    Connection conn = new Koneksi().getConnection();
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel tabMode;
        
public void refresh(ReportView rv){
    rv.buttonTambah.setEnabled(true);
    rv.buttonCetak.setEnabled(true);
}
        
private String validasi(SiswaView sv){
    String ket;        
        if (sv.textNis.getText().equals("")){
            ket = "NIS belum diisi!";
        }
        else if (sv.textNama.getText().equals("")){
            ket = "Nama belum diisi!";
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
        
public void tampil_data(ReportView rv){
        Object[] Baris ={"NIS","Nama Siswa","Kelas","Jenkel","Alamat","No Hp"};
        tabMode = new DefaultTableModel(null, Baris);
        rv.tabelSiswa.setModel(tabMode);   
        String sql ="select * from tb_siswa";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet hasil= pst.executeQuery(sql);
            while(hasil.next()){
                String nis = hasil.getString("nis");
                String nama = hasil.getString("nama_siswa");
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
                String[] data={nis,nama,kelas,jenkel,alamat,nohp};
                tabMode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rv, "Terjadi kesalahan pada tampil data.\nDetails:"+e.toString(),
                    "Kesalahan Tampil Data",JOptionPane.ERROR_MESSAGE);
            System.out.println("Terjadi Kesalahan pada tampil data.\nDetails:"+e.toString());
        }
    }
public void pilih_siswa (SiswaView sv){
        int row = sv.tabelSiswa.getSelectedRow();
        sv.textNis.setText(sv.tabelSiswa.getValueAt(row, 0).toString());
        sv.textNama.setText(sv.tabelSiswa.getValueAt(row, 1).toString());
        sv.comboKelas.setSelectedItem(sv.tabelSiswa.getValueAt(row, 2).toString());
        String jenkel=sv.tabelSiswa.getValueAt(row, 3).toString();
        if (jenkel.equals("Laki-Laki")) {
            sv.radioLaki.setSelected(true);
        }else{
            sv.radioPerempuan.setSelected(true);
        }
        sv.textAlamat.setText(sv.tabelSiswa.getValueAt(row, 4).toString());
        sv.textnohp.setText(sv.tabelSiswa.getValueAt(row, 5).toString());                
        sv.textNis.setEnabled(true);

    }
}
