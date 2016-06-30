package com.company;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.io.File;
import java.util.Hashtable;

public class MyFileView extends FileView {
    private Hashtable icons = new Hashtable(5);
    private Hashtable fileDescriptions = new Hashtable(5);
    private Hashtable typeDescriptions = new Hashtable(5);

    /**
     * The name of the file.  Do nothing special here. Let
     * the system file view handle this.
     */
    public String getName(File f) {
        return null;
    }

    /**
     * Adds a human readable description of the file.
     */
    public void putDescription(File f, String fileDescription) {
        fileDescriptions.put(fileDescription, f);
    }

    /**
     * A human readable description of the file.
     *
     * @see FileView#getDescription
     */
    public String getDescription(File f) {
        return (String) fileDescriptions.get(f);
    }

    ;

    /**
     * Adds a human readable type description for files. Based on "dot"
     * extension strings, e.g: ".gif". Case is ignored.
     */
    public void putTypeDescription(String extension, String typeDescription) {
        typeDescriptions.put(typeDescription, extension);
    }

    /**
     * Adds a human readable type description for files of the type of
     * the passed in file. Based on "dot" extension strings, e.g: ".gif".
     * Case is ignored.
     */
    public void putTypeDescription(File f, String typeDescription) {
        putTypeDescription(getExtension(f), typeDescription);
    }

    /**
     * A human readable description of the type of the file.
     *
     */
    public String getTypeDescription(File f) {
        return (String) typeDescriptions.get(getExtension(f));
    }

    /**
     * Conveinience method that returnsa the "dot" extension for the
     * given file.
     */
    public String getExtension(File f) {
        String name = f.getName();
        if (name != null) {
            int extensionIndex = name.lastIndexOf('.');
            if (extensionIndex < 0) {
                return null;
            }
            return name.substring(extensionIndex + 1).toLowerCase();
        }
        return null;
    }

    /**
     * Adds an icon based on the file type "dot" extension
     * string, e.g: ".gif". Case is ignored.
     */
    public void putIcon(String extension, Icon icon) {
        icons.put(extension, icon);
    }

    /**
     * Icon that reperesents this file. Default implementation returns
     * null. You might want to override this to return something more
     * interesting.
     *
     */
    public Icon getIcon(File f) {
        Icon icon = null;
        String extension = getExtension(f);
        if (extension != null) {
            icon = (Icon) icons.get(extension);
        }
        return icon;
    }

    /**
     * Whether the file is hidden or not. This implementation returns
     * true if the filename starts with a "."
     */
    public Boolean isHidden(File f) {
        String name = f.getName();
        if (name != null && !name.equals("") && name.charAt(0) == '.') {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    ;

    /**
     * Whether the directory is traversable or not. Generic implementation
     * returns true for all directories.
     * <p>
     * You might want to subtype MyFileView to do somethimg more interesting,
     * such as recognize compound documents directories; in such a case you might
     * return a special icon for the diretory that makes it look like a regular
     * document, and return false for isTraversable to not allow users to
     * descend into the directory.
     *
     * @see FileView#isTraversable
     */
    public Boolean isTraversable(File f) {
        if (f.isDirectory()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
