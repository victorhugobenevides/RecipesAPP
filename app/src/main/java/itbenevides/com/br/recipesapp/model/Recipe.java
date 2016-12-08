package itbenevides.com.br.recipesapp.model;

import java.util.List;

/**
 * Created by victorhugo on 26/09/2016.
 */

public class Recipe {
   public String _id;
    public String name;
    public float time;
    public String image;
    public String[] bookmarks;
    public String[] comments;
    public Categories[]  categories;
}
