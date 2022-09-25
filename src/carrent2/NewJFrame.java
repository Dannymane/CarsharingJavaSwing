/*
data wypozyczenia, zwrotu, koszt
 */
package carrent2;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


class samochod 
{
    private String nazwa;
    private String model;
    private double cena_za_dobe;
    
    public String getNazwa()
    {
        return nazwa;
    }
    public void setNazwa(String nazwa_)
    {
        nazwa = nazwa_;
    }
    public String getModel()
    {
        return model;
    }
    public void setModel(String model_)
    {
        model = model_;
    }
    public double getCena_za_dobe()
    {
        return cena_za_dobe;
    }
    public void setCena_za_dobe(double cena_za_dobe_)
    {
        cena_za_dobe = cena_za_dobe_;
    }
    public samochod(String nazwa_, String model_, double cena_za_dobe_)
    {
        nazwa = nazwa_;
        model = model_;
        cena_za_dobe = cena_za_dobe_;
    }
}
class wypozyczalnia
{
    public static List<samochod> lista_samochodow = new ArrayList(5);
    public static String nazwa;
    public static DefaultComboBoxModel model = new DefaultComboBoxModel();
    private static List<wypozyczenie> lista_wypozyczen = new ArrayList(5);
    
    public static List<wypozyczenie> getLista_wypozyczen()
    {
        return lista_wypozyczen;
    }

    public wypozyczalnia(String nazwa_)
    {
        nazwa = nazwa_;
    }
    public void dodaj_samochod(samochod s)
    {
        lista_samochodow.add(s);
    }
    public void dodaj_element_do_model(String element)
    {
        model.addElement(element);
    }
    public static DefaultComboBoxModel zwroc_model()
    {
        return model;
    }
    public static void wpisz_liste_wypozyczen_na_tabele()
    {            

        for (wypozyczenie w : lista_wypozyczen)
        {
            DefaultTableModel model = (DefaultTableModel) NewJFrame.getJTableDane().getModel();
            model.addRow(new Object[] {String.valueOf(w.getNr_wyp()), w.getImie(), w.getNazwisko(), w.getTelefon(), w.getSamochod(),
                w.getData_wypozyczenia(), w.getData_zwrotu()});
        }
    }
    public static void aktualizacja_pliku_listy_wypozyczen() //throws IOException
    {
        try
        {    
            List<String> lista_wypozyczen_string = new ArrayList<>();       
            for (wypozyczenie w : lista_wypozyczen)
            {
                String wypozyczenie_string;
                if (w.getData_zwrotu().equals(""))
                {
                    wypozyczenie_string = String.valueOf(w.getNr_wyp())+" "+w.getImie()+" "+w.getNazwisko()+" "+w.getTelefon()
                    +" "+w.getSamochod()+" "+w.getData_wypozyczenia();
                } else
                {
                    wypozyczenie_string = String.valueOf(w.getNr_wyp())+" "+w.getImie()+" "+w.getNazwisko()+" "+w.getTelefon()
                    +" "+w.getSamochod()+" "+w.getData_wypozyczenia()+" "+w.getData_zwrotu();
                }
                lista_wypozyczen_string.add(wypozyczenie_string);
            }
            Path droga_zapisu = Paths.get(".\\wypozyczenia.txt");
            Files.write(droga_zapisu, lista_wypozyczen_string, StandardCharsets.UTF_8);
        } catch(FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        } catch(IOException ex2)
        {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex2);
            System.out.println(ex2.getMessage());
        }
    }
    public static void usun_wypozyczenie(int index)
    {
        for (wypozyczenie w : lista_wypozyczen)
            {
            if (w.getNr_wyp() == index)
            {
                lista_wypozyczen.remove(w);
                System.out.println("Wypożyczenie o numerze "+index+" usunięto");
                break;
            }
            }
    }
    public static boolean czy_pokrywa_sie_wprowadzane_wypozyczenie_z_innym(String samochod_, String data_wyp_, String data_zw_)
    {
        for (wypozyczenie w : lista_wypozyczen)
        {
            if (w.getSamochod().equals(samochod_))
            {

                try {
                    if(w.getData_zwrotu().equals("") && data_zw_.equals(""))
                    {
                        return true;
                    }
                    if(w.getData_zwrotu().equals(""))
                    {
                        Date d_wyp_istniejaca = new SimpleDateFormat("dd-MM-yyyy").parse(w.getData_wypozyczenia());
                        Date d_wyp_wprowadzana = new SimpleDateFormat("dd-MM-yyyy").parse(data_wyp_);
                        Date d_zw_wprowadzana = new SimpleDateFormat("dd-MM-yyyy").parse(data_zw_);
                        if(d_wyp_istniejaca.before(d_zw_wprowadzana))
                        {
                            return true;
                        }
                        if(d_zw_wprowadzana.before(d_wyp_wprowadzana))
                        {
                            return true;
                        }
                        if(d_wyp_istniejaca.equals(d_wyp_wprowadzana) && !d_zw_wprowadzana.equals(d_wyp_istniejaca))
                        {
                            return true;
                        }
                    }
                    if(data_zw_.equals(""))
                    {
                        Date d_wyp_wprowadzana = new SimpleDateFormat("dd-MM-yyyy").parse(data_wyp_);
                        Date d_zw_istniejaca = new SimpleDateFormat("dd-MM-yyyy").parse(w.getData_zwrotu());
                        Date d_wyp_istniejaca = new SimpleDateFormat("dd-MM-yyyy").parse(w.getData_wypozyczenia());
                        if(d_wyp_wprowadzana.before(d_zw_istniejaca)) 
                        {
                            return true;
                        }
                        if(d_wyp_istniejaca.equals(d_wyp_wprowadzana) && !d_zw_istniejaca.equals(d_wyp_istniejaca))
                        {
                            return true;
                        }
                    }
                        if(!data_zw_.equals("") && !w.getData_zwrotu().equals(""))
                        {    
                        Date d_wyp_istniejaca = new SimpleDateFormat("dd-MM-yyyy").parse(w.getData_wypozyczenia());
                        Date d_wyp_wprowadzana = new SimpleDateFormat("dd-MM-yyyy").parse(data_wyp_);
                        Date d_zw_istniejaca = new SimpleDateFormat("dd-MM-yyyy").parse(w.getData_zwrotu());
                        Date d_zw_wprowadzana = new SimpleDateFormat("dd-MM-yyyy").parse(data_zw_);

                        if(d_wyp_istniejaca.before(d_wyp_wprowadzana) && d_wyp_wprowadzana.before(d_zw_istniejaca)) {
                            return true;
                        }
                        if(d_zw_wprowadzana.before(d_wyp_wprowadzana))
                        {
                            return true;
                        }
                        if(d_wyp_wprowadzana.before(d_wyp_istniejaca) && d_wyp_istniejaca.before(d_zw_wprowadzana))
                        {
                            return true;
                        }
                        if(d_wyp_istniejaca.equals(d_wyp_wprowadzana) && !d_zw_istniejaca.equals(d_wyp_istniejaca))
                        {
                            return true;
                        }
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(wypozyczalnia.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }
    public static boolean zmien_date_zwrotu(int nr_wyp_, String nowa_data_zwrotu)
    {
        for (wypozyczenie w : lista_wypozyczen)
        {
            if (w.getNr_wyp() == nr_wyp_)
            {
                //sprawdzenie czy data wypozyczenia nie jest wcześniejsza od daty zwrotu
                try {
                Date d_wyp_istniejaca = new SimpleDateFormat("dd-MM-yyyy").parse(w.getData_wypozyczenia());
                Date d_zw_ = new SimpleDateFormat("dd-MM-yyyy").parse(nowa_data_zwrotu);
                if(d_zw_.before(d_wyp_istniejaca))
                        {
                            return false;
                        }
                } catch (ParseException ex) {
                    Logger.getLogger(wypozyczalnia.class.getName()).log(Level.SEVERE, null, ex);
                }
                w.setData_zwrotu(nowa_data_zwrotu);
                return true;
            }
        }
        return false;
    }
    
}
class wypozyczenie
{
//    private static int nr = 0;
    private int nr_wyp;
    private String imie;
    private String nazwisko;
    private String telefon;
    private String samochod;    
    private String data_wypozyczenia;
    private String data_zwrotu;
    private double koszt;
    
    
//    public static int getNr()
//    {
//        return nr;
//    }
    public int getNr_wyp()
    {
        return nr_wyp;
    }
    public  String getImie()
    {
        return imie;
    }
    public String getNazwisko()
    {
        return nazwisko;
    }
    public String getTelefon()
    {
        return telefon;
    }
    public String getSamochod()
    {
        return samochod;
    }
    public String getData_wypozyczenia()
    {
        return data_wypozyczenia;
    }
    public String getData_zwrotu()
    {
        return data_zwrotu;
    }    
    public void setData_zwrotu(String data_zwrotu_)
    {
        data_zwrotu = data_zwrotu_;
    }
    public wypozyczenie(int nr_wyp_, String imie_, String nazwisko_, String telefon_, String samochod_, String data_wypozyczenia_,
           String data_zwrotu_)
    {
        imie = imie_;
        nazwisko = nazwisko_;
        telefon = telefon_;
        samochod = samochod_;
        data_wypozyczenia = data_wypozyczenia_;
        data_zwrotu = data_zwrotu_;
//        nr++;
        nr_wyp = nr_wyp_;
        
    }
}
public class NewJFrame extends javax.swing.JFrame {

    public static void main(String args[]) throws IOException {

    try
    {
        wypozyczalnia w = new wypozyczalnia("Wypożyczalnia samochodów Daniela");
        Path droga = Paths.get("Baza.txt");
        List<String> lines = Files.readAllLines(droga);
        System.out.println("---Wczytano samochody z bazy:---");
        for (String line : lines)
        {
            String[] tablica_slow = line.split(" ");
            w.dodaj_samochod(new samochod((tablica_slow[0]+" "+tablica_slow[1]),
            tablica_slow[2],Double.parseDouble(tablica_slow[3])));
            System.out.println(line);
            w.dodaj_element_do_model(line);
        }
    } catch(FileNotFoundException ex1)
    {
        System.out.println(ex1.getMessage());
    }
    
    try
    {
        Path droga = Paths.get("Wypozyczenia.txt");
        List<String> lines = Files.readAllLines(droga);
        System.out.println("---Wczytano wypozyczenia z bazy:---");
        for (String line : lines)
        {
            String[] tablica_slow = line.split(" ");
            if (tablica_slow.length == 10)
            {
                wypozyczalnia.getLista_wypozyczen().add(new wypozyczenie(parseInt(tablica_slow[0]), tablica_slow[1],
            tablica_slow[2], tablica_slow[3],tablica_slow[4]+" "+tablica_slow[5]+" "+tablica_slow[6]+" "+tablica_slow[7],
            tablica_slow[8], tablica_slow[9]));
            } else
            {
                wypozyczalnia.getLista_wypozyczen().add(new wypozyczenie(parseInt(tablica_slow[0]), tablica_slow[1],
            tablica_slow[2], tablica_slow[3],tablica_slow[4]+" "+tablica_slow[5]+" "+tablica_slow[6]+" "+tablica_slow[7],
                        tablica_slow[8], ""));
            }
            
            System.out.println(line);
        }
    } catch(FileNotFoundException ex1)
    {
        System.out.println(ex1.getMessage());
    }    
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });


}
    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
        ////  Zapis wypożyczeń z pliku do  tabeli. jTableDane.getModel() dziala tylko w tym miejscu
        for (wypozyczenie wy : wypozyczalnia.getLista_wypozyczen())
        {
            DefaultTableModel model = (DefaultTableModel) jTableDane.getModel();
            model.addRow(new Object[] {String.valueOf(wy.getNr_wyp()), wy.getImie(), wy.getNazwisko(), wy.getTelefon(), wy.getSamochod(),
                wy.getData_wypozyczenia(), wy.getData_zwrotu()});
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cbSamochod = new javax.swing.JComboBox<>();
        tfNazwisko = new javax.swing.JTextField();
        bUsun = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        tfData_wypozyczenia = new javax.swing.JTextField();
        tfTelefon = new javax.swing.JTextField();
        bDodaj = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfImie = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        bOd_dzis = new javax.swing.JButton();
        tfData_zwrotu = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDane = new javax.swing.JTable();
        jZakoncz_wynajem = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cbSamochod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbSamochod.setModel(wypozyczalnia.zwroc_model());
        cbSamochod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSamochodActionPerformed(evt);
            }
        });

        tfNazwisko.setText("");

        bUsun.setText("Usun");
        bUsun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUsunActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Telefon");

        tfData_wypozyczenia.setText("");
        tfData_wypozyczenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfData_wypozyczeniaActionPerformed(evt);
            }
        });

        tfTelefon.setText("");

        bDodaj.setText("Dodaj");
        bDodaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDodajActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Imie");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText("Nazwisko");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Data wypożyczenia");

        tfImie.setText("");
        tfImie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfImieActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("Samochód");

        bOd_dzis.setText("Od dziś");
        bOd_dzis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOd_dzisActionPerformed(evt);
            }
        });

        tfData_zwrotu.setText("");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Data zwrotu");

        jTableDane.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nr", "Imie", "Nazwisko", "Telefon", "Samochód", "Data_wypozyczenia", "Data_zwrotu"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDane.setToolTipText("");
        jTableDane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(jTableDane);
        jTableDane.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (jTableDane.getColumnModel().getColumnCount() > 0) {
            jTableDane.getColumnModel().getColumn(0).setMinWidth(25);
            jTableDane.getColumnModel().getColumn(0).setPreferredWidth(25);
            jTableDane.getColumnModel().getColumn(4).setMinWidth(170);
            jTableDane.getColumnModel().getColumn(5).setMinWidth(70);
            jTableDane.getColumnModel().getColumn(6).setMinWidth(70);
        }

        jZakoncz_wynajem.setText("Zakoncz wynajem");
        jZakoncz_wynajem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jZakoncz_wynajemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(bDodaj)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jZakoncz_wynajem, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(bUsun)
                .addGap(51, 51, 51))
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(tfData_zwrotu))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(tfData_wypozyczenia, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tfTelefon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(tfNazwisko, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfImie, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbSamochod, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bOd_dzis, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfImie))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfNazwisko, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfTelefon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbSamochod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfData_wypozyczenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bOd_dzis))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfData_zwrotu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bDodaj)
                    .addComponent(jZakoncz_wynajem)
                    .addComponent(bUsun))
                .addGap(221, 221, 221))
        );

        setBounds(0, 0, 1139, 442);
    }// </editor-fold>//GEN-END:initComponents

    private void cbSamochodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSamochodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSamochodActionPerformed

    private void bUsunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUsunActionPerformed
        int selectedRow = jTableDane.getSelectedRow();
        int nr_wyp_do_usuniencia = parseInt((String) jTableDane.getValueAt(selectedRow, 0));
        if(selectedRow != -1)
        {
            DefaultTableModel model = (DefaultTableModel) jTableDane.getModel();
            model.removeRow(selectedRow);
            wypozyczalnia.usun_wypozyczenie(nr_wyp_do_usuniencia);
//            try
//            {
                wypozyczalnia.aktualizacja_pliku_listy_wypozyczen();
//            }catch (IOException ex) {
//                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
//                System.out.println(ex.getMessage());
//            }
        }
    }//GEN-LAST:event_bUsunActionPerformed

    private void bDodajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDodajActionPerformed
        String imie = tfImie.getText();
        String nazwisko = tfNazwisko.getText();
        String telefon = tfTelefon.getText();
        String samochod = cbSamochod.getSelectedItem().toString();
        String data_wypozyczenia = tfData_wypozyczenia.getText();
        String data_zwrotu = tfData_zwrotu.getText();
        String nr = "1";
        if (wypozyczalnia.getLista_wypozyczen().size() != 0){
            nr = String.valueOf(wypozyczalnia.getLista_wypozyczen().get(wypozyczalnia.getLista_wypozyczen().size()-1).getNr_wyp()+1);
        }
        //data
        Pattern regex_data  = Pattern.compile
        ("(?:0[1-9]|[12][0-9]|3[01])-(?:0[1-9]|1[012])-202[2-9]{1}");
        Matcher matcher_data_1 = regex_data.matcher(data_wypozyczenia);
        Matcher matcher_data_2 = regex_data.matcher(data_zwrotu);
        
        //telefon
        Pattern regex_tel  = Pattern.compile("[0-9]{9}");
        Matcher matcher_tel = regex_tel.matcher(telefon);
        
        Pattern regex_tel_rozsz  = Pattern.compile("\\+48[0-9]{9}");
        Matcher matcher_tel_rozsz = regex_tel_rozsz.matcher(telefon);
        
        //imie, nazwisko
        Pattern regex_imie_nazw  = Pattern.compile("[A-Z]{1}[a-z]{2,}");
        Matcher matcher_imie = regex_imie_nazw.matcher(imie);
        Matcher matcher_nazwisko = regex_imie_nazw.matcher(nazwisko);
        if(matcher_tel.matches() != true && matcher_tel_rozsz.matches() != true)
        {
            JOptionPane.showMessageDialog(this, "Niepoprawny telefon!");
            return;
        }
        if(matcher_imie.matches() != true || matcher_nazwisko.matches() != true)
        {
            JOptionPane.showMessageDialog(this, "Niepoprawne imie lub nazwisko!");
            return;
        }
            if(matcher_data_1.matches() == true && ((matcher_data_2.matches()==true) || (data_zwrotu.equals(""))))
            {
//                nr = wypozyczenie.getNr_wyp()+1;
//                if(!imie.isEmpty() && !nazwisko.isEmpty() && !telefon.isEmpty() && !data_wypozyczenia.isEmpty())
//                {
                    if(!wypozyczalnia.czy_pokrywa_sie_wprowadzane_wypozyczenie_z_innym(samochod, data_wypozyczenia, data_zwrotu))
                            {
                                //zapis do tabeli
                               DefaultTableModel model = (DefaultTableModel) jTableDane.getModel(); 
                               model.addRow(new Object[] {nr, imie, nazwisko, telefon, samochod, data_wypozyczenia, data_zwrotu});
                               //tworzenie instancji
                               wypozyczalnia.getLista_wypozyczen().add(new wypozyczenie(parseInt(nr), imie, nazwisko, 
                                   telefon, samochod, data_wypozyczenia, data_zwrotu));
                               //aktualizacja pliku wypozyczenia
           //                    try
           //                    {
                                   wypozyczalnia.aktualizacja_pliku_listy_wypozyczen();
           //                    }catch (IOException ex) {
           //                        Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
           //                        System.out.println(ex.getMessage());
           //                    }                               
                            }else
                    {
                        JOptionPane.showMessageDialog(this, "Samochód jest wypozyczony w podanym okresie\n"
                                + "lub wykryto wypożyczenia bez daty zwrotu, przeszkadzające kolejnym wypozyczeniom \n"
                                + "lub data zwrotu nie może być wcześniejsza od daty wypozyczenia");
                    }
//                }else
//                {
//                    JOptionPane.showMessageDialog(this, "Proszę wypełnić wszystkie pola! Data zwrotu może być pusta.");
//                }
        }else
            {
                JOptionPane.showMessageDialog(this, "Niepoprawny format daty");
            }
        
        
    }//GEN-LAST:event_bDodajActionPerformed

    private void bOd_dzisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOd_dzisActionPerformed
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDateTime now = LocalDateTime.now();  
        tfData_wypozyczenia.setText(format.format(now));
    }//GEN-LAST:event_bOd_dzisActionPerformed

    private void tfImieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfImieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfImieActionPerformed

    private void tfData_wypozyczeniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfData_wypozyczeniaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfData_wypozyczeniaActionPerformed

    private void jZakoncz_wynajemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jZakoncz_wynajemActionPerformed
        int selectedRow = jTableDane.getSelectedRow();
        String data_zw = (String) jTableDane.getValueAt(selectedRow, 6);
        if(selectedRow != -1 && data_zw.equals(""))
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
            LocalDateTime now = LocalDateTime.now();
            int nr_wyp = parseInt((String) jTableDane.getValueAt(selectedRow, 0));
            if(wypozyczalnia.zmien_date_zwrotu(nr_wyp, format.format(now)))
            {
                jTableDane.setValueAt(format.format(now),selectedRow, 6);
                wypozyczalnia.aktualizacja_pliku_listy_wypozyczen();
            }
        }else
        {
            JOptionPane.showMessageDialog(this, "Nie można zmienić istniejącą datę zwrotu!");
        }
    }//GEN-LAST:event_jZakoncz_wynajemActionPerformed




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDodaj;
    private javax.swing.JButton bOd_dzis;
    private javax.swing.JButton bUsun;
    private javax.swing.JComboBox<String> cbSamochod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JTable jTableDane;
    private javax.swing.JButton jZakoncz_wynajem;
    private javax.swing.JTextField tfData_wypozyczenia;
    private javax.swing.JTextField tfData_zwrotu;
    private javax.swing.JTextField tfImie;
    private javax.swing.JTextField tfNazwisko;
    private javax.swing.JTextField tfTelefon;
    // End of variables declaration//GEN-END:variables
    public static javax.swing.JTable getJTableDane()
    {
        return jTableDane;
    }
}
