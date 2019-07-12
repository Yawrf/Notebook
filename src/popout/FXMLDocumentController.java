/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package popout;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import note.Note;
import note.NoteWriter;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    //<editor-fold>
    @FXML TextField titleField;
    @FXML TextArea descriptionArea;
    @FXML TextArea bodyArea;
    @FXML ListView referencesList;
    @FXML ListView tagsList;
    //</editor-fold>
    
    Stage thisStage = null;
    NoteWriter writer = new NoteWriter();
    
//  General Methods ------------------------------------------------------------
    
    public void closePopout() {
        try {
            thisStage.close();
            notebook.FXMLDocumentController.popoutArray.remove(thisStage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void referencePopout() {
        String reference = (String) referencesList.getSelectionModel().getSelectedItem();
        referencesList.getSelectionModel().clearSelection();
        if(reference != null && !reference.isEmpty()) {
            Note note = writer.readNote(reference);
            System.out.println(note);
            notebook.FXMLDocumentController.openReferencePopout(note);
        }
    }
    
//  Note Injection Methods -----------------------------------------------------
    
    public void injectNote(Note note, Stage thisStage) {
        titleField.setText(note.getTitle());
        descriptionArea.setText(note.getDescription());
        bodyArea.setText(note.getBody());
        ObservableList references = FXCollections.observableArrayList(note.getReferences());
        ObservableList tags = FXCollections.observableArrayList(note.getTags());
        referencesList.setItems(references);
        tagsList.setItems(tags);
        
        this.thisStage = thisStage;
        thisStage.setTitle("Notebook by Yawrf - " + note.getTitle() + " - Popout");
        thisStage.show();
    }
    
//  ----------------------------------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
