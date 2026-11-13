package station.model;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable 
public class Position {
    
    private int x;
    private int y;
	
    public Position() {
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

    public boolean estVoisineDe(Position autre) { // Permet de calculer si deux cases sont voisines

        int ecartX = Math.abs(this.x - autre.x); // Distance en colonnes entre deux cases
        int ecartY = Math.abs(this.y - autre.y); // Distance en lignes entre deux cases

        return ecartX + ecartY == 1; // vaut 1 seulement si une coordonnée est identique et que l'autre change de 1 => case voisine

    }

    @Override 
    public boolean equals(Object o) { // Permet de savoir si l'objet reçu est la même case que la position

        if (this == o) return true; // Comparaison a soi-même
        if (!(o instanceof Position)) return false; // Exclu les objets de type non Position

        Position autre = (Position) o; // Permet de traiter l'objet telle qu'une position pour accéder au x et y
        return x == autre.x && y == autre.y; // Même case si x et y sont les mêmes

    }

    @Override 
    public int hashCode() { // Transforme la position en un nombre qui sera toujours le même pour une même combinaison de coordonnées

        return Objects.hash(x, y);

    }

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}

}
