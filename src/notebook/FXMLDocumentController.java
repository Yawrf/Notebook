/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebook;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    @FXML ListView referencesList;
    @FXML ListView noteTagsList;
    @FXML ListView tagsList;
    @FXML ListView notesList;
    
    //</editor-fold>
    
    NoteWriter writer = new NoteWriter();
    Note currentNote = null;
    Tag currentTag = null;
    
//  Note Methods ---------------------------------------------------------------
    
    public void newNote() {
        openNoteEditorScreen();
    }
    
    public void editNote() {
        if(currentNote != null) {
            new_edit.FXMLDocumentController controller = openNoteEditorScreen();
            controller.editNote(currentNote);
        }
    }
    
    public void deleteNote() {
        if(currentNote != null) {
            Alert confirmation = new Alert(AlertType.WARNING, "You are about to delete a note.\nThis can not be undone.", ButtonType.OK, ButtonType.CANCEL);
            confirmation.showAndWait();

            if(confirmation.getResult() == ButtonType.OK) {
                writer.deleteNote(currentNote.getTitle());
                for(String s : currentNote.getTags()) {
                    Tag tag = writer.readTag(s);
                    tag.removeNote(currentNote.getTitle());
                    if(tag.getNotes().isEmpty()) {
                        writer.deleteTag(s);
                    } else {
                        writer.writeTag(tag);
                    }
                }
                clearFields();
                updateNotes();
                updateTags();
            }
        }
    }
    
    public void viewCurrentNote() {
        
        if(currentNote != null) {
            titleField.setText(currentNote.getTitle());
            descriptionArea.setText(currentNote.getDescription());
            bodyArea.setText(currentNote.getBody());
            
            ObservableList<String> references = FXCollections.observableArrayList(new ArrayList<>());
            referencesList.setItems(references);
            references = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(currentNote.getReferences())));
            referencesList.setItems(references);
            
            ObservableList<String> tags = FXCollections.observableArrayList(new ArrayList<>());
            noteTagsList.setItems(tags);
            tags = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(currentNote.getTags())));
            noteTagsList.setItems(tags);
        } else {
            titleField.setText("");
            descriptionArea.setText("");
            bodyArea.setText("");
            
            ObservableList<String> empty = FXCollections.observableArrayList(new ArrayList<>());
            referencesList.setItems(empty);
            noteTagsList.setItems(empty);
        }
        
    }
    
    public void clearFields() {
        currentNote = null;
        viewCurrentNote();
    }
    
//  List Methods ---------------------------------------------------------------
    
    public void updateTags() {
        ObservableList<String> noList = FXCollections.observableArrayList(new ArrayList<>());
        tagsList.setItems(noList);
        ObservableList<String> tags = FXCollections.observableArrayList(writer.getTags());
        tagsList.setItems(tags);
        System.out.println("Tags Updated");
    }
    
    public void updateNotes() {
        ObservableList<String> noList = FXCollections.observableArrayList(new ArrayList<>());
        notesList.setItems(noList);
        ObservableList<String> notes = FXCollections.observableArrayList(writer.getNotes());
        notesList.setItems(notes);
        filterNotes();
        System.out.println("Notes Updated");
    }
    
    public void filterNotes() {
        if(currentTag != null) {
            ArrayList<String> remove = new ArrayList<>();
            ArrayList<String> notes = new ArrayList<>();
                notes.addAll(notesList.getItems());
            for(String s : notes) {
                if(!currentTag.getNotes().contains(s)) {
                    remove.add(s);
                }
            }
           notesList.getItems().removeAll(remove);
        }
    }
    
    public void tagSelect() {
        String tagString = (String) tagsList.getSelectionModel().getSelectedItem();
        
        Tag tag = writer.readTag(tagString);
        currentTag = tag;
        updateNotes();
        
        tagsList.getSelectionModel().clearSelection();
    }
    
    public void noteSelect() {
        
        currentNote = writer.readNote((String) notesList.getSelectionModel().getSelectedItem());
        viewCurrentNote();
        notesList.getSelectionModel().clearSelection();
        
    }
    
    public void clearTag() {
        
        currentTag = null;
        updateNotes();
        
    }
    
    public void referenceSelect() {
        String reference = (String) referencesList.getSelectionModel().getSelectedItem();
        
        if(reference != null && !reference.isEmpty()) {
            Note note = writer.readNote(reference);
            referencesList.getSelectionModel().clearSelection();
            openReferencePopout(note);
        }
    }
    
//  Window Methods -------------------------------------------------------------
    
    public final static Stage editScreen = new Stage();
    public final static ArrayList<Stage> popoutArray = new ArrayList<>();
    
    private new_edit.FXMLDocumentController openNoteEditorScreen() {
        try {
            
            FXMLLoader loader = new FXMLLoader(new_edit.EditPage.class.getResource("FXMLDocument.fxml"));
            Parent root = loader.load();
            new_edit.FXMLDocumentController editorController = (new_edit.FXMLDocumentController) loader.getController();
            
            editScreen.setScene(new Scene(root));
            editScreen.show();
            
            return editorController;
        } catch (Exception e) {
            System.out.println(e);
            for(StackTraceElement s : e.getStackTrace()) {
                System.out.println(s);
            }
        }
        return null;
    }
    
    public void closeNoteEditorScreen() {
        editScreen.close();
        updateTags();
        updateNotes();
    }
    
    public static void openReferencePopout(Note note) {
        try {
            
            FXMLLoader loader = new FXMLLoader(popout.Popout.class.getResource("FXMLDocument.fxml"));
            Parent root = loader.load();
            popout.FXMLDocumentController popoutController = (popout.FXMLDocumentController) loader.getController();
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            popoutArray.add(stage);
            
            popoutController.injectNote(note, stage);
        } catch (Exception e) {
            System.out.println(e);
            for(StackTraceElement s : e.getStackTrace()) {
                System.out.println(s);
            }
        }
    }
    
    public void closeAllPopouts() {
        for(Stage s : popoutArray) {
            s.close();
        }
    }
    
    public void exitApp() {
        
        Alert confirmExit  = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.CANCEL);
        confirmExit.showAndWait();
        
        if(confirmExit.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }
    
    public void exit() {
        editScreen.close();
        closeAllPopouts();
    }
    
//  ----------------------------------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        updateTags();
        updateNotes();
        
        editScreen.setTitle(new_edit.EditPage.stageTitle);
        editScreen.setOnHidden(event -> {
            updateTags();
            updateNotes();
            if(currentNote != null) {
                currentNote = writer.readNote(currentNote.getTitle());
                viewCurrentNote();
            }
        });
        
    }    
    
}
