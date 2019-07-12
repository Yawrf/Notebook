/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package note;

import java.io.Serializable;

/**
 *
 * @author rewil
 */
public class Note implements Serializable {
    
    private final String title;
    private String description;
    private String body;
    private String[] references;
    private String[] tags;
    
    public Note(String title, String description, String body, String[] references, String[] tags) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.references = references;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String[] getReferences() {
        return references;
    }

    public void setReferences(String[] references) {
        this.references = references;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
    
    @Override
    public String toString() {
        return title;
    }
    
}
