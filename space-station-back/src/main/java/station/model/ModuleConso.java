package station.model;
import jakarta.persistence.Column;

public class ModuleConso extends Module {

    @Column(nullable = false)
    private int nbConsoModules;

    public ModuleConso () {}

    public ModuleConso (Integer id, String nom, int nbAstroModuleMax, int nbModuleMax, boolean debloque, Position position,
            StationSpatiale station, int nbConsoModules) {
        super (id, nom, nbAstroModuleMax, nbModuleMax, debloque, position, station);

        this.nbConsoModules = nbConsoModules;
    }

    public int getNbConsoModules() {
        return nbConsoModules;
    }

    public void setNbConsoModules(int prodModules) {
        this.nbConsoModules = nbConsoModules;
    }

    @Override
    public String toString() {
        return "ModuleConso [consoModules=" + nbConsoModules + "id=" + getId() + ", nom=" + getNom() + ", nbAstroModuleMax=" 
        + getNbAstroModuleMax() + ", nbModuleMax=" + getNbModuleMax() + ", debloque=" + isDebloque() + ", position=" 
        + getPosition() + "]";
    }
}
