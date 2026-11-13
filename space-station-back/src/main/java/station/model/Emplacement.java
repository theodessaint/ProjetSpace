package station.model;

import station.enumerator.ModuleEnum;

public class Emplacement { // Pour attribuer l'emplacement des modules directement dans la grille de manière fixe

private final ModuleEnum type;
private final Position position;

public Emplacement(ModuleEnum type, Position position) {
    this.type = type;
    this.position = position;
}

public ModuleEnum getType() {
    return type;
}

public Position getPosition() {
    return position;
}

@Override
public String toString() {
    return "Emplacement [type=" + type + ", position=" + position + "]";
}

}
