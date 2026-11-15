package station.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import station.enumerator.ModuleEnum;

@Entity 
public class Emplacement { // Pour attribuer l'emplacement des modules directement dans la grille de manière fixe

@Enumerated(EnumType.STRING)
private final ModuleEnum type;

@Embedded 
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
