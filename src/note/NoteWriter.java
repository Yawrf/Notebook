/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package note;

import filewriter.Writer;
import java.util.ArrayList;

/**
 *
 * @author rewil
 */
public class NoteWriter {
    
    private Writer writer = new Writer("Notebook");
    private final String notePath = "Notes";
    private final String tagPath = "Tags";
    
//  Notes ----------------------------------------------------------------------
    
    /**
     * Saves a note that's passed in as a file in Program Files/Yawrf/Notebook/Notes on the drive the program is installed on
     * @param note 
     */
    public void writeNote(Note note) {
        
        writer.moveDownFolder(notePath);
        writer.writeObject(note, note.getTitle());
        writer.moveUpFolder();
        
    }
    
    /**
     * Returns a note that's saved with a title that's passed in if it's saved in Program Files/Yawrf/Notebook/Notes
     * If there's no corresponding file, returns null
     * @param title
     * @return 
     */
    public Note readNote(String title) {
        
        writer.moveDownFolder(notePath);
        Note note = null;
        try{
            note = (Note) writer.readObject(title);
        } catch (Exception e) {}
        writer.moveUpFolder();
        
        return note;
    }
    
    /**
     * Deletes a note with a title that's passed in from Program Files/Yawrf/Notebook/Notes
     * @param title 
     */
    public void deleteNote(String title) {
        
        writer.moveDownFolder(notePath);
        writer.deleteFile(title);
        writer.moveUpFolder();
        
    }
    
    /**
     * Returns a list of all Notes saved in Program Filese/Yawrf/Notebook/Notes
     * @return 
     */
    public ArrayList<String> getNotes() {
        
        writer.moveDownFolder(notePath);
        ArrayList<String> output = writer.listFiles();
        writer.moveUpFolder();
        
        return output;
    }
    
// Tags ------------------------------------------------------------------------
    
    /**
     * Saves a tag that's passed in as a file in Program Files/Yawrf/Notebook/Tags on the drive the program is installed on
     * @param tag 
     */
    public void writeTag(Tag tag) {
        
        writer.moveDownFolder(tagPath);
        writer.writeObject(tag, tag.getName());
        writer.moveUpFolder();
        
    }
    
    /**
     * Returns a note that's saved with a title that's passed in if it's saved in Program Files/Yawrf/Notebook/Tags
     * If there's no corresponding file, returns null
     * @param name
     * @return 
     */
    public Tag readTag(String name) {
        
        writer.moveDownFolder(tagPath);
        Tag tag = null;
        try{
            tag = (Tag) writer.readObject(name);
        } catch (Exception e) {}
        writer.moveUpFolder();
        
        return tag;
    }
    
    /**
     * Deletes a tag with a title that's passed in from Program Files/Yawrf/Notebook/Tags
     * @param name 
     */
    public void deleteTag(String name) {
        
        writer.moveDownFolder(tagPath);
        writer.deleteFile(name);
        writer.moveUpFolder();
        
    }
    
    /**
     * Returns a list of all Tags saved in Program Files/Yawrf/Notebook/Tags
     * @return 
     */
    public ArrayList<String> getTags() {
        
        writer.moveDownFolder(tagPath);
        ArrayList<String> output = writer.listFiles();
        writer.moveUpFolder();
        
        return output;
    }
    
}
