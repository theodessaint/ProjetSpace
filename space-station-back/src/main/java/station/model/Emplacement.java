package station.model;

import station.enumerator.ModuleEnum;

public class Emplacement { // Pour attribuer l'emplacement des modules directement dans la grille de manière fixe
    
    ModuleEnum type;
    Position position;
    
    public Emplacement() {
	}

    public Emplacement(ModuleEnum type, Position position) {
		this.type = type;
		this.position = position;
	}

	public ModuleEnum getType() {
		return type;
	}
	public void setType(ModuleEnum type) {
		this.type = type;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Emplacement [type=" + type + ", position=" + position + "]";
	}
    
}
