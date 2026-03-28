package drinkshop.domain;

import java.util.ArrayList;
import java.util.List;

public class Reteta {

    private int id;
    private List<IngredientReteta> ingrediente;

    public Reteta(int id, List<IngredientReteta> ingrediente) {
        this.id = id;
        this.ingrediente = new ArrayList<>(ingrediente);
    }

    public void setIngrediente(List<IngredientReteta> ingrediente) {
        this.ingrediente = new ArrayList<>(ingrediente);
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public List<IngredientReteta> getIngrediente() {
        return ingrediente;
    }

    @Override
    public String toString() {
        return "Reteta{" +
                "productId=" + id +
                ", ingrediente=" + ingrediente +
                '}';
    }
}

