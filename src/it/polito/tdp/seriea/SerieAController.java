/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {
	
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSquadra"
    private ChoiceBox<Team> boxSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnSelezionaSquadra"
    private Button btnSelezionaSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaAnnataOro"
    private Button btnTrovaAnnataOro; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaCamminoVirtuoso"
    private Button btnTrovaCamminoVirtuoso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	Team t = boxSquadra.getValue();
    	
    	if( t==null) {
    		txtResult.appendText("Devi selezionare una squadra");
    		return;
    	}
    	
    	Map<Season, Integer> punteggi = model.calcolaPunteggi(t);
    	txtResult.clear();
    	
    	for(Season s : punteggi.keySet()) {
    		txtResult.appendText(s.getDescription()+" "+punteggi.get(s)+"\n");
    	}
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
 
    	Season annata = model.calcolaAnnataDOro();
    	int deltaPesi = model.getMass();
    	txtResult.appendText("L'annata d'oro è : "+ annata.getDescription()+" "+deltaPesi);
    	
    	btnTrovaAnnataOro.setDisable(false);
        btnTrovaCamminoVirtuoso.setDisable(false);
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {

    }
    
    public void setModel(Model m) {
    	this.model =m;
    	
    	boxSquadra.getItems().clear();
    	boxSquadra.getItems().addAll(model.getSquadre());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }
}
