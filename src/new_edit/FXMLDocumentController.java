/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package new_edit;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import note.Note;
import note.NoteWriter;
import note.Tag;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    //<editor-fold>
    @FXML TextField titleField;
    @FXML TextArea descriptionArea;
    @FXML TextArea bodyArea;
    @FXML ListView referencesIn;
    @FXML ListView referencesOut;
    @FXML ListView tagsIn;
    @FXML ListView tagsOut;
    @FXML TextField newTag;
    //</editor-fold>
    
    private NoteWriter writer = new NoteWriter();
    
//  General Methods ------------------------------------------------------------
    
    public void saveNote() {
        String title = titleField.getText();
        if(!title.isEmpty()) {
            Alert overwrite = new Alert(AlertType.WARNING, "There is already a saved note with this title.\nSaving this note will overwrite the previously saved note.\nThis is irreversible.", ButtonType.OK, ButtonType.CANCEL);
            boolean overwriteBoo = false;
            if(writer.getNotes().contains(title)) {
                overwrite.showAndWait();
                overwriteBoo = true;
            }
            if(!overwriteBoo || overwrite.getResult() == ButtonType.OK) {
                String description = descriptionArea.getText();
                String body = bodyArea.getText();
                String[] referencesList = new String[referencesOut.getItems().size()];
                    for(int i = 0; i < referencesList.length; ++i) {
                        referencesList[i] = (String) referencesOut.getItems().get(i);
                    }
                String[] tagsList = new String[tagsOut.getItems().size()];
                    for(int i = 0; i < tagsList.length; ++i) {
                        tagsList[i] = (String) tagsOut.getItems().get(i);
                    }

                Note note = new Note(title, description, body, referencesList, tagsList);
                writer.writeNote(note);
                checkTags();
                closeWindow(true);
            }
        } else {
            Alert empty = new Alert(AlertType.ERROR, "Title Field cannot be empty.", ButtonType.OK);
            empty.showAndWait();
        }
    }
    
    public void closeWindow() {
        closeWindow(false);
    }
    
    private void closeWindow(boolean fromSave) {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Are you sure you want to close this window?", ButtonType.YES, ButtonType.NO);
        if(!fromSave) {
            confirm.showAndWait();
        }
        if(fromSave || confirm.getResult() == ButtonType.YES) {
            notebook.FXMLDocumentController.editScreen.close();
        }
    }
    
    public void referenceIn() {
        String reference = (String) referencesIn.getSelectionModel().getSelectedItem();
        if(reference != null && !reference.isEmpty()) {
            referencesOut.getItems().add(reference);
            referencesIn.getItems().remove(reference);
        }
        referencesIn.getSelectionModel().clearSelection();
    }
    
    public void referenceOut() {
        String reference = (String) referencesOut.getSelectionModel().getSelectedItem();
        if(reference != null && !reference.isEmpty()) {
            referencesOut.getItems().remove(reference);
            referencesIn.getItems().add(reference);
        }
        referencesOut.getSelectionModel().clearSelection();
    }
    
    public void tagIn() {
        String tag = (String) tagsIn.getSelectionModel().getSelectedItem();
        if(tag != null && !tag.isEmpty()) {
            tagsOut.getItems().add(tag);
            tagsIn.getItems().remove(tag);
        }
        tagsIn.getSelectionModel().clearSelection();
    }
    
    public void tagOut() {
        String tag = (String) tagsOut.getSelectionModel().getSelectedItem();
        if(tag != null && !tag.isEmpty()) {
            tagsOut.getItems().remove(tag);
            tagsIn.getItems().add(tag);
        }
        tagsOut.getSelectionModel().clearSelection();
    }
    
    public void newTagIn() {
        String tag = (String) newTag.getText();
        if(tag != null && !tag.isEmpty()) {
            tagsOut.getItems().add(tag);
            newTag.setText("");
        }
    }
    
    private void checkTags() {
        ArrayList<String> tags = writer.getTags();
        ArrayList<String> newTags = new ArrayList<>();
            newTags.addAll(tagsOut.getItems());
        for(String s : newTags) {
            if(tags.contains(s)) {
                Tag tag = writer.readTag(s);
                tag.addNote(titleField.getText());
                writer.writeTag(tag);
            } else {
                Tag tag = new Tag(s);
                tag.addNote(titleField.getText());
                writer.writeTag(tag);
            }
        }
        for(String s : oldTags) {
            if(!newTags.contains(s)) {
                Tag tag = writer.readTag(s);
                tag.removeNote(titleField.getText());
                if(tag.getNotes().isEmpty()) {
                    writer.deleteTag(s);
                } else {
                    writer.writeTag(tag);
                }
            }
        }
    }
    
    public void referencePopout(MouseEvent e) {
        if(e.getButton() == MouseButton.SECONDARY) {
            String reference = (String) referencesIn.getSelectionModel().getSelectedItem();
            referencesIn.getSelectionModel().clearSelection();
            if(reference != null && !reference.isEmpty()) {
                Note note = writer.readNote(reference);
                notebook.FXMLDocumentController.openReferencePopout(note);
            }
        }
    }
    
//  Edit Injection Methods -----------------------------------------------------
    
    ArrayList<String> oldTags = new ArrayList<>();
    
    public void editNote(Note note) {
        titleField.setText(note.getTitle());
        descriptionArea.setText(note.getDescription());
        bodyArea.setText(note.getBody());
        
        ObservableList<String> references = FXCollections.observableArrayList(note.getReferences());
        referencesOut.setItems(references);
        ObservableList<String> tags = FXCollections.observableArrayList(note.getTags());
        tagsOut.setItems(tags);
        
        referencesIn.getItems().removeAll(references);
        tagsIn.getItems().removeAll(tags);
        
        oldTags.addAll(Arrays.asList(note.getTags()));
    }
    
//  ----------------------------------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ObservableList<String> references = FXCollections.observableArrayList(writer.getNotes());
        referencesIn.setItems(references);
        ObservableList<String> tags = FXCollections.observableArrayList(writer.getTags());
        tagsIn.setItems(tags);
    }    
    
}
