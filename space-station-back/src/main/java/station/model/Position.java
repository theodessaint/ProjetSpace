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
	public int hashCode() { // Transforme la position en un nombre qui sera toujours le même pour une même combinaison de coordonnées
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) { // Permet de savoir si l'objet reçu est la même case que la position
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
