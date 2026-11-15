package station.model;
import jakarta.persistence.Column;

public class ModuleProd extends Module {

    @Column(nullable = false)
    private int nbProdModules;

    public ModuleProd () {}

    public ModuleProd (Integer id, String nom, int nbAstroModuleMax, int nbModuleMax, boolean debloque, Position position,
            StationSpatiale station, int nbProdModules) {
        super (id, nom, nbAstroModuleMax, nbModuleMax, debloque, position, station);

        this.nbProdModules = nbProdModules;
    }

    public int getNbProdModules() {
        return nbProdModules;
    }

    public void setNbProdModules(int prodModules) {
        this.nbProdModules = nbProdModules;
    }

    @Override
    public String toString() {
        return "ModuleProd [prodModules=" + nbProdModules + "id=" + getId() + ", nom=" + getNom() + ", nbAstroModuleMax=" 
        + getNbAstroModuleMax() + ", nbModuleMax=" + getNbModuleMax() + ", debloque=" + isDebloque() + ", position=" 
        + getPosition() + "]";
    }
    
    





}
