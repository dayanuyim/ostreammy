package com.dayanuyim.ostreammy.utils;

import java.io.File;
import static java.lang.String.*;

public class UriFileMapper
{
    private File base_file;
    private String base_uri;

    public UriFileMapper(String base_uri, File base_file)
    {
        this.base_uri = base_uri;
        this.base_file = base_file;
    }

    public String getBaseUri(){ return base_uri;}
    public File getBaseFile(){ return base_file;}

    public static boolean isAncestor(File ancestor, File f)
    {
        String base_path = ancestor.getAbsolutePath();
        String path = f.getAbsolutePath();

        return isAncestor(base_path, path);
    }

    public static boolean isAncestor(String base_path, String path)
    {
        if(!path.startsWith(base_path))
            return false;

        if(path.length() > base_path.length()){
            char c = path.charAt(base_path.length());
            if(c != '/' && c != '\\')
                return false;
        }

        return true;
    }

    public static String getSubpath(String base_path, String path)
    {
        if(!isAncestor(base_path, path))
            throw new RuntimeException(format("Bad Mapping, Path '%s' is not based on %s.", path, base_path));
    
        return path.substring(base_path.length());
    }

    public String toUri(File f)
    {
        String base_path = base_file.getAbsolutePath();
        String path = f.getAbsolutePath();

        String sub_path = getSubpath(base_path, path).replaceAll("\\", "/");
        return base_uri + sub_path;
    }

    public File toFile(String uri)
    {
        String sub_uri = getSubpath(base_uri, uri);
        return new File(base_file, sub_uri);
    }
}

