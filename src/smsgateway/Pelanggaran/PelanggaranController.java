package smsgateway.Pelanggaran;
import java.awt.HeadlessException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import smsgateway.pengaturan.Koneksi;

public class PelanggaranController {
    private final Connection conn = new Koneksi().getConnection(); 
    PreparedStatement pst;     
    ResultSet rs;
    DefaultTableModel tabMode;    
    
    private void clear_item_pelanggaran(PelanggaranView pv){
        try{
            for(int a=pv.tabelItemPelanggaran.getRowCount()-1; a >= 0;  a--){
                tabMode.removeRow(a);             
                pv.tabelItemPelanggaran.setModel(tabMode);
            }                           
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Terjadi kesalahan pada method clear item pelanggaran. \nDetails:   "+ex.toString());
        }
    }
    private String kode_otomatis(){       
        String kode="";
        try {            
            int kodeLama;
            pst = conn.prepareStatement("select no_pelanggaran from tb_pelanggaran order by no_pelanggaran desc limit 1");            
            rs = pst.executeQuery();
            if (!rs.next()){                
                kode = "PLG-0001";                
            }else{
                kodeLama = Integer.parseInt(rs.getString(1).substring(4))+1;              
                if (kodeLama < 10){
                    kode="PLG-000"+kodeLama;
                }else if(kodeLama >= 10 && kodeLama < 100){
                    kode="PLG-00"+kodeLama;
                }else if(kodeLama >= 100 && kodeLama < 1000){
                    kode="PLG-0"+kodeLama;
                }else{
                    kode="PLG-"+kodeLama;
                }
            }            
        } catch (SQLException ex) {
            System.out.println("Terjadi kesalahan pada kode pelanggaran otomatis.\nDetails: "+ex.toString());            
        }
        return kode;
    }
    
    public String validasi(PelanggaranView pv) {
        String ket="";
        java.util.Date tgl =  pv.textTanggal.getDate();
        if (tgl==null){
            JOptionPane.showMessageDialog(null,"Tanggal Pelanggaran belum diisi!",null,JOptionPane.ERROR_MESSAGE);        
        }else if (PelanggaranView.textNisSiswa.getText().equals("") || PelanggaranView.textNamaSiswa.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Data pelanggaran belum diisi!",null,JOptionPane.ERROR_MESSAGE);
        }else if(pv.tabelItemPelanggaran.getRowCount() <= 0){
            JOptionPane.showMessageDialog(null,"Data pelanggaran masih kosong!",null,JOptionPane.ERROR_MESSAGE);         
        }else if(pv.textSisaPointPelanggaran.getText().equals("0")){
            JOptionPane.showMessageDialog(null,"Textbox point belum diisi!",null,JOptionPane.ERROR_MESSAGE);         
        }else{
            ket = "Sukses";
        }
        return ket;
    }
    
    public void refresh(PelanggaranView pv){
            pv.textNoPelanggaran.setText(kode_otomatis());
            PelanggaranView.textNisSiswa.setText("");
            PelanggaranView.textNamaSiswa.setText("");
            PelanggaranView.textSisaPoint.setText("");
            PelanggaranView.textKodePelanggaran.setText("");
            PelanggaranView.textNamaPelanggaran.setText("");
            PelanggaranView.textKategori.setText("");
            PelanggaranView.textPointPelanggaran.setText("0");
            pv.textJumlahPelanggaran.setText("0");
            pv.textTotalPointPelanggaran.setText("0");
            pv.textSisaPointPelanggaran.setText("0");
            pv.textTotalPointSiswa.setText("0");
            clear_item_pelanggaran(pv);
            //menonaktifkan textfield dan button
             pv.textNoPelanggaran.setEnabled(false);
            PelanggaranView.textNisSiswa.setEnabled(true);
            PelanggaranView.textNamaSiswa.setEnabled(false);
            PelanggaranView.textSisaPoint.setEnabled(false);
            PelanggaranView.textKodePelanggaran.setEnabled(true);
            PelanggaranView.textNamaPelanggaran.setEnabled(false);
            PelanggaranView.textKategori.setEnabled(false);
            PelanggaranView.textPointPelanggaran.setEnabled(false);
            pv.textJumlahPelanggaran.setEnabled(true);
            pv.textTotalPointPelanggaran.setEnabled(false);
            pv.textSisaPointPelanggaran.setEnabled(true);
            pv.textTotalPointSiswa.setEnabled(false);
            pv.buttonBatal.setEnabled(true);
            pv.buttonCariPelanggaran.setEnabled(true);
            pv.buttonCariSiswa.setEnabled(true);
            pv.buttonSimpan.setEnabled(true);
            pv.buttonTambahPelanggaran.setEnabled(true);
            pv.buttonHapusItem.setEnabled(true);
    }
       

    public void cari_siswa(PelanggaranView pv){
         try {
             rs = pst.executeQuery("select nama_siswa from tb_siswa where nis = '"+ PelanggaranView.textNisSiswa.getText() +"'");
             if (rs.next()){
                 PelanggaranView.textNamaSiswa.setText(rs.getString(1));
                 PelanggaranView.textSisaPoint.setText(rs.getString(2));
                 PelanggaranView.textSisaPointPelanggaran.setText(rs.getString(3));
             }else{
                  JOptionPane.showMessageDialog(pv,"Data siswa tidak ditemukan!","Not Found", JOptionPane.ERROR_MESSAGE);
             }
         } catch (SQLException ex) {
             JOptionPane.showMessageDialog(pv,"Terjadi kesalahan pada cari siswa. \nDetails: "+ex.toString());
             Logger.getLogger(PelanggaranController.class.getName()).log(Level.SEVERE, null, ex);
         }
}

    public void cari_pelanggaran(PelanggaranView pv){     
        try {
             rs = pst.executeQuery("select * from tb_point where kode_pelanggaran = '" + PelanggaranView.textKodePelanggaran.getText() + "'");
             if (rs.next()){
                 PelanggaranView.textNamaPelanggaran.setText(rs.getString("nama_pelanggaran")); //menampilkan isi data dari kolom nama_barang di databse ke textfield
                 PelanggaranView.textKategori.setText(rs.getString("kategori"));
                 PelanggaranView.textPointPelanggaran.setText(rs.getString("point"));             
                 pv.textJumlahPelanggaran.requestFocus(); //mengatur posisi kursor agar fokus ke textfield jumlah beli
             }else{
                 JOptionPane.showMessageDialog(pv,"Data pelanggaran tidak ditemukan!","Not Found",JOptionPane.ERROR_MESSAGE);
             }
         } catch (SQLException ex) {
             Logger.getLogger(PelanggaranController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    public void tambah_item_pelanggaran(PelanggaranView pv){
        try{
            if(PelanggaranView.textKodePelanggaran.getText().equals("") &&PelanggaranView.textNamaPelanggaran.getText().equals("")){
                 JOptionPane.showMessageDialog(pv,"Pilih dulu pelanggaran yang ingin ditambahkan!","Not Found",JOptionPane.ERROR_MESSAGE);
            }else if(pv.textJumlahPelanggaran.getText().equals("0")){
                JOptionPane.showMessageDialog(pv,"Jumlah pelanggaran belum diisi!!","Not Found",JOptionPane.ERROR_MESSAGE);            
                pv.textJumlahPelanggaran.requestFocus();
                pv.textJumlahPelanggaran.setText("0");
                pv.textJumlahPelanggaran.requestFocus();
            }
            else{
                tabMode = (DefaultTableModel) pv.tabelItemPelanggaran.getModel();
                ArrayList data = new ArrayList();
                data.add(PelanggaranView.textKodePelanggaran.getText());
                data.add(PelanggaranView.textNamaPelanggaran.getText());
                data.add(PelanggaranView.textPointPelanggaran.getText());
                data.add(pv.textJumlahPelanggaran.getText());
                BigDecimal point = new BigDecimal(PelanggaranView.textPointPelanggaran.getText());
                BigDecimal jumlahPelanggaran = new BigDecimal(pv.textJumlahPelanggaran.getText());
                BigDecimal totalPoint = point.multiply(jumlahPelanggaran);
                data.add(totalPoint);                
                tabMode.addRow(data.toArray()); 
                hitung_total(pv);
                PelanggaranView.textKodePelanggaran.setText("");
                PelanggaranView.textNamaPelanggaran.setText("");
                PelanggaranView.textKategori.setText("");
                PelanggaranView.textPointPelanggaran.setText("0");
                pv.textJumlahPelanggaran.setText("0");                                                
             }            
        }catch(HeadlessException | NumberFormatException ex){
              JOptionPane.showMessageDialog(pv,"Gagal menambahkan item pelanggaran! \nDetails:"+ex.toString(),
                      "Not Found",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void hapus_item_pelanggaran(PelanggaranView pv){
        try{
               int row = pv.tabelItemPelanggaran.getSelectedRow();
               if (row < 0){
                   JOptionPane.showMessageDialog(null, "Pilih dulu item yang ingin dihapus!");
               }else{                   
                   tabMode.removeRow(row);
                   pv.tabelItemPelanggaran.setModel(tabMode);
                   hitung_total(pv);
               }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada proses hapus item belanja.Details\n"+e.toString());
        }
    }
    
    public void hitung_total(PelanggaranView pv){
        BigDecimal total = new BigDecimal(0);        
        for(int a=0; a<pv.tabelItemPelanggaran.getRowCount(); a++){
            total= total.add(new BigDecimal( pv.tabelItemPelanggaran.getValueAt(a, 4).toString()));
        }
        pv.textTotalPointPelanggaran.setText(total.toString());
    }
    
    
    public void hitung_kembali(PelanggaranView pv){
        BigDecimal bayar = new BigDecimal(0);
        if(!pv.textSisaPointPelanggaran.getText().equals("")){
            bayar = new BigDecimal(pv.textSisaPointPelanggaran.getText());
        }
        BigDecimal totalpoint = new BigDecimal(pv.textTotalPointPelanggaran.getText());
        BigDecimal kembali = bayar.subtract(totalpoint);
        pv.textTotalPointSiswa.setText(kembali.toString());
    }
    
    public void simpan_pelanggaran(PelanggaranView pv){
        if(validasi(pv).equals("Sukses")){
            try {
                int isSucces;
                Date d = pv.textTanggal.getDate();
                java.sql.Date tgl = new java.sql.Date(d.getTime());
                pst = conn.prepareStatement("insert into tb_pelanggaran values (?,?,?,?,?,?,?)");
                pst.setString(1, pv.textNoPelanggaran.getText());
                pst.setString(2, tgl.toString());
                pst.setString(3, PelanggaranView.textNisSiswa.getText());                
                pst.setBigDecimal(4, new BigDecimal(pv.textTotalPointPelanggaran.getText()));
                pst.setBigDecimal(5, new BigDecimal(pv.textSisaPointPelanggaran.getText()));
                pst.setBigDecimal(6, new BigDecimal(pv.textTotalPointSiswa.getText()));
                pst.setString(7, "admin");
                isSucces = pst.executeUpdate();
                if (isSucces == 1){
                    simpan_item_pelanggaran(pv);                    
                }
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");                
                refresh(pv);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada Simpan Data: Details \n"+ex.toString(),"",JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(PelanggaranController.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }    
    }
    
    public void simpan_item_pelanggaran(PelanggaranView pv){
        for(int a = 0; a <= pv.tabelItemPelanggaran.getRowCount()-1; a++){
            try {
                pst = conn.prepareStatement("insert into tb_detail_pelanggaran (no_pelanggaran,kode_pelanggaran,qty, sub_total) values(?,?,?,?)");
                pst.setString(1, pv.textNoPelanggaran.getText());
                pst.setString(2, pv.tabelItemPelanggaran.getValueAt(a, 0).toString());
                pst.setInt(3, Integer.parseInt(pv.tabelItemPelanggaran.getValueAt(a, 3).toString()));
                pst.setBigDecimal(4,new BigDecimal(pv.tabelItemPelanggaran.getValueAt(a, 4).toString()));
                pst.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada simpan item: Details\n"+ex.toString(),"",JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(PelanggaranController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
