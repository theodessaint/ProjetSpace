package station.model;

public abstract class Module {

    private Integer id;
    private String nom;
    private int astroModuleMax;

    public Module () {}

    public Module (Integer id, String nom, int astroModuleMax) {
        this.id = id;
        this.nom = nom;
        this.astroModuleMax = astroModuleMax;
    }

    public Integer getId() {
            return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
        
    public String getNom() {
        return nom;
    }
        
        public void setNom(String nom) {
        this.nom = nom;
    }

    public int getastroModuleMax(){
        return astroModuleMax;
    }

    public void setastroModuleMax(int astroModuleMax){
        this.astroModuleMax = astroModuleMax;
    }

    public String toString() {
		return "Module [id=" + id + ", nom=" + nom + ", astroModuleMax=" + astroModuleMax +  "]";
	}

}