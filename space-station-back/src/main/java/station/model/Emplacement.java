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

private final boolean ouvertAuDepart; // Modules débloqués de base au lancement de la partie

@Embedded 
private final Position position;

public Emplacement(ModuleEnum type, boolean ouvertAuDepart, Position position) {
    this.type = type;
    this.ouvertAuDepart = ouvertAuDepart;
    this.position = position;
}

public boolean isOuvertAuDepart() {
    return ouvertAuDepart;
}

}
