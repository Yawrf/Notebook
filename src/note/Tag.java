/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package note;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author rewil
 */
public class Tag implements Serializable{
    
    private final String name;
    private ArrayList<String> notes = new ArrayList<>();
    
    public Tag(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void addNote(String title) {
        if(!notes.contains(title)) {
            notes.add(title);
        }
    }
    
    public boolean removeNote(String title) {
        return notes.remove(title);
    }
    
    public ArrayList<String> getNotes() {
        return notes;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
