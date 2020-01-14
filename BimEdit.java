// Lausekielinen ohjelmointi II, syksy 2019.
//
// Harjoitustyö.
//
// Juha Lähteenmäki (juha.lahteenmaki@tuni.fi).
//
// Lataa komentoriviparametrina annetun tekstitiedoston sisältämän vaatimusten mukaisen binäärisen ASCII-kuvan,
// sekä samassa tekstitiedostossa annetut kuvan tiedot. Komentoriviparametrina annetulla "echo" -komennolla 
// kaiuttaa saamansa komennot. Suorittaa seuraavat toiminnot: kuvan tulostus näytölle (print),
// kuvan merkkien kääntäminen (invert), kuvan koon ja merkkimäärien tulostaminen (info), edustamerkkien lisääminen (dilate),
// taustamerkkien lisääminen (erode), kuvan uudelleen lataaminen tiedostosta (load), sekä ohjelman sulkeminen (quit).


import java.util.Scanner;
import java.io.File;

public class BimEdit{

    public static final Scanner LUKIJA = new Scanner(System.in);

    public static final String KAIUTA = "echo";
    public static final String TULOSTA = "print";
    public static final String INFO = "info";
    public static final String KÄÄNNÄ = "invert";
    public static final String DILATE = "dilate";
    public static final String ERODE = "erode";
    public static final String LATAA = "load";
    public static final String LOPETA = "quit";

    public static final char[] MERKIT = new char[2];
    public static final int[] KOKO = new int[2];


    public static char[][] lataaKuvaTaulukkoon(String tiedostonimi) {
    // Luo kaksiulotteisen char-alkioiden taulukon parametrina saamansa tiedosto-
    // nimen osoittaman tiedoston sisältämästä mallitaulukosta. Lisäksi täyttää kaksi yksiulotteista
    // aputaulukkoa, jotka sisältävät tiedostosta luettavat rivi- ja sarakekoot sekä
    // taulukossa käytetyt kaksi merkkiä. Lisäksi hylkää vääränmuotoiset taulukot.    
        try {
            File tiedosto = new File(tiedostonimi);
            Scanner tiedostonLukija = new Scanner(tiedosto);      
            
            KOKO[0] = Integer.parseInt(tiedostonLukija.nextLine());
            KOKO[1] = Integer.parseInt(tiedostonLukija.nextLine());
            // Etsitään kuvataulun kokotiedot ja talletaan ne aputaulukkoon.

            char[][] tulostaulu = new char[KOKO[0]][KOKO[1]];
            // Luodaan oikean kokoinen taulukko tiedoston kopioimista varten.
    
            if (KOKO[0] >= 3 && KOKO[1] >= 3 && MERKIT.length == 2) {    
                MERKIT[0] = tiedostonLukija.nextLine().charAt(0);
                MERKIT[1] = tiedostonLukija.nextLine().charAt(0);  
                // Tallennetaan taulukon alkuinfosta taulukossa käytetyt merkit aputaulukkoon.
                    
                int riviInd = 0;
                while (tiedostonLukija.hasNextLine()) {
                    String tulosrivi = tiedostonLukija.nextLine();
                    int sarakeInd = 0;
                    while (sarakeInd < KOKO[1]) {
                        tulostaulu[riviInd][sarakeInd] = tulosrivi.charAt(sarakeInd);
                        sarakeInd++;
                    }
                    // Kopioidaan tiedoston sisältö luotuun taulukkoon rivi kerrallaan.
                    if (tulosrivi.length() != KOKO[1] && MERKIT[0] != ' ' && MERKIT[1] != ' '){
                    // Keskeyttää lukemisen ja palauttaa nullin jos jollakin rivillä on eri määrä
                    // merkkejä kuin alkutiedoissa. Ei huomioi alun tai lopun välilyöntimerkkejä
                    // jos ne on esitiedoissa merkitty taulukon toiseksi väriksi.
                        tiedostonLukija.close();
                        return null;
                    }  
                riviInd++;
                }

                tiedostonLukija.close();
                boolean tarkistus = tarkistaTaulukko(tulostaulu);
                // Tarkistetaan onko kuviossa muita kuin esitiedoissa ilmoitettuja merkkejä,
                // sekä rivimäärän oikeellisuuden.
                if (tarkistus){
                    return tulostaulu;
                }
                else{
                    return null;
                }            
            }
            else{
                tiedostonLukija.close();
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }
    
    
    public static boolean tarkistaTaulukko(char[][] taulu){
    // Tarkistaa että parametrinä annetussa taulukossa on vain 
    // aputaulukossa määriteltyjä merkkejä, ja että taulukon rivimäärä
    // on sama kuin tiedoston alkutiedoissa. Palauttaa true, jos kaikki on kunnossa.
        Boolean tarkista = true;
        if (taulu != null){
            if (taulu.length != KOKO[0]){
                tarkista = false;
            }
            for (int riviInd = 0; riviInd < KOKO[0]; riviInd++){
                for (int sarakeInd = 0; sarakeInd < KOKO[1]; sarakeInd++){
                    if (taulu[riviInd][sarakeInd] != MERKIT[0] && taulu[riviInd][sarakeInd] != MERKIT[1]){
                        tarkista = false;
                    }
                }
            }
            return tarkista;
        }
        else{
            tarkista = false;
            return tarkista;
        }
    }    


    public static void laskeJaTulostaMerkit(char[][] taulu){
    // Laskee ja tulostaa parametrinä saamansa taulukon
    // edusta- ja taustamerkkien määrän, sekä tulostaa 
    // aputaulukoiden perusteella taulukon rivi- ja sarakemäärään.
        if (taulu != null){
            char tausta = MERKIT[0];
            char edusta = MERKIT[1];
            // Haetaan merkit aputaulukosta.
            int taustaMäärä = 0;
            int edustaMäärä = 0;
            for(int riviInd = 0; riviInd < taulu.length; riviInd++){
                for(int sarakeInd = 0; sarakeInd < taulu[0].length; sarakeInd++){
                    if(taulu [riviInd][sarakeInd] == edusta){
                        edustaMäärä = edustaMäärä + 1;
                    }
                    else if(taulu [riviInd][sarakeInd] == tausta){                        
                        taustaMäärä = taustaMäärä + 1;   
                    }   
                }
            }
            System.out.println(KOKO[0] + " x " + KOKO[1]);
            // Tulostetaan koko aputaulukosta saaduilla arvoilla.
            System.out.println(tausta + " " + taustaMäärä);
            System.out.println(edusta + " " + edustaMäärä);
        }    
    }


    public static void vaihdaMerkit(char[][] taulu){
    // Saa parametrinaan kaksiulotteisen taulukon ja
    // korvaa kaikki ensimmäisen merkin esiintymät toisella
    // merkillä ja päinvastoin.
        if(taulu != null && taulu.length > 0 && taulu[0].length > 0){
        // Varmistetaan ettei taulu ole null tai kooltaan 0.
            char tausta = MERKIT[0];
            char edusta = MERKIT[1];
            // Haetaan merkit aputaulukosta.
            for(int riviInd = 0; riviInd < taulu.length; riviInd++){
                for(int sarakeInd = 0; sarakeInd < taulu[0].length; sarakeInd++){
                    if(taulu [riviInd][sarakeInd] == edusta){
                        taulu [riviInd][sarakeInd] = tausta;
                        // muutetaan merkki
                    }
                    else if(taulu [riviInd][sarakeInd] == tausta){                        
                        taulu [riviInd][sarakeInd] = edusta;   
                        // muutetaan merkki              
                    }   
                }
            }
            MERKIT[0] = edusta;
            MERKIT[1] = tausta;            
        }
    }


    public static char[][] SuurennaTaiPienennä(char[][] taulu, String[] komento){
    // Suorittaa parametrina saamalleen taulukolle dilaation tai eroosion, riippuen
    // parametrina saamansa komennon sisältävän taulukon komennoista. Palauttaa muutetun taulukon.
        if(taulu != null && taulu.length > 0 && taulu[0].length > 0){
            char tausta = MERKIT[0];
            char edusta = MERKIT[1];
            // alustetaan merkit dilaation vaatimiksi.
           
            if (komento[0].equals(ERODE)){
            // Vaihdetaan merkit päikseen jos komento onkin erode.
                tausta = MERKIT[1];
                edusta = MERKIT[0];
            }
            
            char[][] uusitaulu = new char[taulu.length][taulu[0].length];
            for(int riviInd = 0; riviInd < taulu.length; riviInd++){
                for(int sarakeInd = 0; sarakeInd < taulu[0].length; sarakeInd++){
                    uusitaulu[riviInd][sarakeInd] = taulu[riviInd][sarakeInd];
                }
            }
            // Luodaan uusi taulukko johon muutokset tehdään, ja
            // kopioidaan alkuperäisen taulun sisältö uuteen taulukkoon.
            
            int dilKoko = Integer.parseInt(komento[1]);
            // Otetaan dilaatiossa käytettävä koko taulukosta.
            int keskipiste = (dilKoko - 1) / 2;
            // Lasketaan liikuteltavan neliön keskipisteen lähtökoordinaatit.

            for (int riviInd = keskipiste; riviInd < (taulu.length - keskipiste); riviInd++){
                for (int sarakeInd = keskipiste; sarakeInd < (taulu[0].length - keskipiste); sarakeInd++){
                    if (taulu[riviInd][sarakeInd] == tausta){
                    // tutkitaan naapurimerkit jos neliön keskellä on taustamerkki.
                        for (int riviInd2 = riviInd - keskipiste; riviInd2 < (riviInd + keskipiste + 1); riviInd2++){
                            for (int sarakeInd2 = sarakeInd - keskipiste; sarakeInd2 < (sarakeInd + keskipiste + 1)
                                ; sarakeInd2++){
                                if(taulu[riviInd2][sarakeInd2] == edusta){
                                // Tarkistetaan onko naapurimerkeissä edustamerkkejä.
                                    uusitaulu[riviInd][sarakeInd] = edusta;
                                    // Lisätään uuteen taulukkoon muutettu merkki tarvittaessa.
                                }
                            }
                        }
                    }
                }
            }
            return(uusitaulu);
        }
        else{
            return null;
        }
    }


    public static String[] tarkistaPitkäKomento(String komento, char[][] taulu){
    // Tarkistaa onko komento kaksiosainen, ja jos on, sen onko se oikean muotoinen.
    // Komennon oltava muotoa dilate x tai erode x, siten, että x on pariton luku, joka on
    // pienempi kuin kuin kuvan leveys tai korkeus, sekä vähintään 3.
    // Tällöin palauttaa komennon alkuosan sisältävän taulukon. Muutoin palauttaa nullin.   
        try{
            String[] osat = komento.split("[ ]");
            if (osat.length != 2){
                return null;
            }
            String alkuKomento = osat[0];
            int dilKoko = Integer.parseInt(osat[1]);
            int tarkistusNro = dilKoko % 2;
            if (dilKoko < 3 || dilKoko > KOKO[0] || dilKoko > KOKO[1] || tarkistusNro == 0){
            // Tarkistaa että numero on määrätynlainen.
                return null;
            }
            if ((alkuKomento.equals(DILATE) || alkuKomento.equals(ERODE))){
            // Tarkistaa että komennon alkuosa on sallittu.
                return osat;
            }
            else{
                return null;
            }
        } catch (NumberFormatException e){
            return null;
            // Hoitaa poikkeuksen jos viimeinen argumentti ei ole int.
        }
    }
        

    public static void tulosta2d(char[][] taulu) {
    // Tulostaa parametrinaan saamansa kaksiulotteisen taulukon alkiot.
        if (taulu != null) {
            for (int ind = 0; ind < taulu.length; ind++) {
                System.out.print(taulu[ind]);
                if (ind < taulu.length - 1) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    } 


    public static void main(String args[]){
        System.out.println("-----------------------");
        System.out.println("| Binary image editor |");
        System.out.println("-----------------------");
        boolean kaiku = false;
        if (args.length <= 2 && args.length > 0 && args != null){
            if (args.length == 2 && !(args[1].equals(KAIUTA))){
                System.out.println("Invalid command-line argument!");
                System.out.println("Bye, see you soon.");
            }
            else{
                if(args.length == 2 && args[1].equals(KAIUTA)){
                    kaiku = true;
                }
                String tiedostonimi = args[0];
                Boolean jatketaan = true;
                char[][] kuvataulu = lataaKuvaTaulukkoon(tiedostonimi);
                if (kuvataulu == null){
                    System.out.println("Invalid image file!");
                    System.out.println("Bye, see you soon.");
                    jatketaan = false;
                }
                while(jatketaan){
                    System.out.println("print/info/invert/dilate/erode/load/quit?");
                    String komento = LUKIJA.nextLine();
                    if (kaiku){
                        System.out.println(komento);
                    }
                    String[] pitkäKomento = tarkistaPitkäKomento(komento, kuvataulu);
                    // Tarkistaa mahdollisen dilaatio- tai eroosiokomennon.
                    if (komento.equals(LOPETA)){
                        System.out.println("Bye, see you soon.");
                        jatketaan = false;
                    }
                    else if (komento.equals(TULOSTA)){
                        tulosta2d(kuvataulu);
                    }
                    else if (komento.equals(INFO)){
                        laskeJaTulostaMerkit(kuvataulu);
                    }
                    else if (komento.equals(KÄÄNNÄ)){
                        vaihdaMerkit(kuvataulu);
                    }
                    else if (komento.equals(LATAA)){
                        kuvataulu = lataaKuvaTaulukkoon(tiedostonimi);
                    }
                    else if (pitkäKomento != null){
                        kuvataulu = SuurennaTaiPienennä(kuvataulu, pitkäKomento);
                    }
                    else{
                        System.out.println("Invalid command!");
                    }                   
                }
            }
        }
        else{
            System.out.println("Invalid command-line argument!");
            System.out.println("Bye, see you soon.");
        }
    }
}
