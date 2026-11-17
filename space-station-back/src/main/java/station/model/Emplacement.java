package station.model;

import station.enumerator.ModuleEnum;

public class Emplacement { // Pour attribuer l'emplacement des modules directement dans la grille de manière fixe

private final ModuleEnum type;

private final boolean ouvertAuDepart; // Modules débloqués de base au lancement de la partie

private final Position position;

public Emplacement(ModuleEnum type, Position position, boolean ouvertAuDepart) {
    this.type = type;
    this.position = position;
    this.ouvertAuDepart = ouvertAuDepart;
}

public ModuleEnum getType() {
	return type;
}

public boolean isOuvertAuDepart() {
	return ouvertAuDepart;
}

public Position getPosition() {
	return position;
}
}
