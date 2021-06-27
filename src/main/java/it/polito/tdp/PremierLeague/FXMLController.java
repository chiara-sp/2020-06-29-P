/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	
    	txtResult.clear();
    	if(model.pesoMassimo()==0) {
    		txtResult.appendText("creare prima il grafo");
    		return;
    	}
    	txtResult.appendText("Coppie con connessione massima\n");
    	for(Adiacenza a: model.getMax()) {
    		txtResult.appendText(a.getM1()+ " - "+a.getM2()+ "("+ a.getPeso()+")\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	int min; 
    	try {
    		min= Integer.parseInt(txtMinuti.getText());
    	}catch(NumberFormatException e) {
    		txtResult.appendText("inserire un numero");
    		return;
    	}
    	
    	Integer mese= cmbMese.getValue();
    	if(mese==null) {
    		txtResult.appendText("selezioanre un mese");
    		return;
    	}
    	model.creaGrafo(mese, min);
    	
    	txtResult.appendText("Grafo creato\n");
    	txtResult.appendText("#vertici: "+model.numVertici()+ "\n");
    	txtResult.appendText("#archi: "+model.numArchi()+"\n");
    	
    	cmbM1.getItems().clear();
    	cmbM1.getItems().addAll(model.getVertici());
    	cmbM2.getItems().clear();
    	cmbM2.getItems().addAll(model.getVertici());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    	txtResult.clear();
    	if(model.pesoMassimo()==0) {
    		txtResult.appendText("creare prima il grafo");
    		return ;
    	}
    	
    	Match partenza = cmbM1.getValue();
    	Match arrivo= cmbM2.getValue();
    	if(partenza==null || arrivo==null) {
    		txtResult.appendText("selezionare i due vertici da collegare");
    		return;
    	}
    	List<Match> risultato= model.ricorsione(partenza, arrivo);
    	txtResult.appendText("trovato cammino con "+model.getPesoMAx()+" di peso\n");
    	for(Match m: risultato) {
    		txtResult.appendText(m+ "\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Integer> mesi= new LinkedList<>();
    	for(int i=1; i<=12; i++) {
    		mesi.add(i);
    	}
    	cmbMese.getItems().addAll(mesi);
  
    }
    
    
}
