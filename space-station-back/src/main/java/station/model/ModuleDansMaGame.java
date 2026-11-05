package station.model;

import station.model.Module;

public class ModuleDansMaGame extends Module {
    private Integer id;
    private int ModuleMax;

    public ModuleDansMaGame () {}

    public ModuleDansMaGame (Integer id, String nom, int astroModuleMax, int ModuleMax) {
        super(id, nom, astroModuleMax);
        this.id = id;
        this.ModuleMax = ModuleMax;
    }

    public Integer getId() {
            return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getModuleMax(){
        return ModuleMax;
    }

    public void setModuleMax(int ModuleMax){
        this.ModuleMax = ModuleMax;
    }

    public String toString() {
		return "Module [id=" + id + ", ModuleMax=" + ModuleMax +  "]";
	}

}