package station.model;

import station.model.Module;

public class ModuleDansMaGame extends Module {

    private int ModuleMax;

    public ModuleDansMaGame () {}

<<<<<<< HEAD
    public ModuleDansMaGame (Integer id, String nom, int astroModuleMax, int ModuleMax) {
=======
    public ModuleDansMaGame(Integer id, String nom, int astroModuleMax, int moduleMax) {
>>>>>>> origin/main
        super(id, nom, astroModuleMax);
        
        this.ModuleMax = moduleMax;
    }

    public int getModuleMax(){
        return ModuleMax;
    }

    public void setModuleMax(int ModuleMax){
        this.ModuleMax = ModuleMax;
    }

    @Override
    public String toString() {
        return "ModuleDansMaGame [id=" + id + ", nom=" + nom + ", ModuleMax=" + ModuleMax + ", astroModuleMax="
                + astroModuleMax + ", getId()=" + getId() + ", getModuleMax()=" + getModuleMax() + ", getNom()="
                + getNom() + ", getastroModuleMax()=" + getastroModuleMax() + ", toString()=" + super.toString()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
    }
}