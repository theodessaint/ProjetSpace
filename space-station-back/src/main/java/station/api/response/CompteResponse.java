package station.api.response;

import station.model.Compte;

public class CompteResponse {
    private Integer id;
    private String login;

    public static CompteResponse convert(Compte compte) {
        CompteResponse response = new CompteResponse();

        response.id = compte.getId();
        response.login = compte.getLogin();

        return response;
    }
    public Integer getId() {
        return id;
    }   
    
    public String getLogin() {
        return login;
    } 
}
