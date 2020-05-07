package com.example.photoapplication09.model;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String caption;
    private String location;
    private File file;
    private Date date;
    private Map<String, Set<String>> tags;
    /**
     * Constructor to make a photo requires a file
     * @param file
     *
     */
    public Photo(File file){

        this.file = file;
        this.caption = "";
        this.date = new Date(file.lastModified());
        this.tags = new HashMap<>();
        this.tags.put("person", new HashSet<String>());
        this.tags.put("location", new HashSet<String>());
    }

    /**
     * Constructor to make a photo requires a file
     * @param file
     * @param caption of photo
     *
     */
    public Photo(File file, String caption){

        this.file = file;
        this.caption = caption;
        this.date = new Date(file.lastModified());
        this.tags = new HashMap<>();
        this.tags.put("person", new HashSet<String>());
        this.tags.put("location", new HashSet<String>());

    }

    public Photo(String location, Date dateTaken) {
        this.location = location;
        this.date = dateTaken;
        this.tags = new HashMap<>();
        this.tags.put("person", new HashSet<String>());
        this.tags.put("location", new HashSet<String>());

    }

    /**
     * Retrieves location
     * @return photo filepath
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * method returns name of photo
     * @return String of name
     */
    public String getName() {

        return name;
    }
    /**
     * method returns name of caption
     * @return String of caption
     *
     */
    public String getCaption() {

        return this.caption;
    }
    /**
     * method returns to set name of caption
     * @param newCaption of new Caption
     * @return null
     *
     */
    public void setCaption (String newCaption) {

        this.caption = newCaption;
    }
    /**
     * method remove caption
     * @return void
     *
     */
    public void removeCaption () {

        this.caption = "";
    }
    /**
     * method returns ArrayList of Tags
     * @return ArrayList of Tags
     *
     */
    public Map<String, Set<String>> getTags() {
        return this.tags;
    }

    /**
     * method returns amount of tags
     * @return int of size
     *
     */
    public int getTagsQuantity(){

        return this.tags.size();
    }

    /**
     * method returns date of photo
     * @return String of date format
     *
     */
    public String getDateString(){
        return new SimpleDateFormat("MM/dd/yy").format(this.getDate());
    }
    /**
     * method return date in string
     * @return String of date
     *
     */
    public String getDate(){

        return this.date+"";
    }

    /**
     * method returns file  used
     * @return String File of picture
     *
     */
    public File getFile() {
        return this.file;
    }
    /**
     * method compare the name of a photo to another
     * @param other of what you want to compare
     * @return boolean if true or false
     *
     */
    public boolean equals(Photo other) {

        return this.name.equals(other.name);
    }
    /**
     * method returns path of a photo
     * @return String of path
     *
     */
    public String getPath() {

        return this.file.getPath();
    }

    public void deleteTag(String key) {
        if(tags.containsKey(key)) {
            tags.remove(key);
        } else {

        }
    }

    public void deleteTagValue(String key, String value) {
        if(tags.containsKey(key) && tags.get(key).contains(value)) {
            tags.get(key).remove(value);
        } else {

        }
    }

    public void addTag(String key) {
        if(tags.containsKey(key)) {
        } else {
            tags.put(key, new HashSet<String>());
        }
    }

    public void addTag(String key, String value) {
        Set<String> values = tags.getOrDefault(key, new HashSet<String>());
        values.add(value);
        tags.put(key, values);
    }
}
