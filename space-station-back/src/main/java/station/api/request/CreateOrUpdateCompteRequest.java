
package station.api.request;
 
import jakarta.validation.constraints.NotBlank;
 
public class CreateOrUpdateCompteRequest {
    private Integer id;
 
    @NotBlank(message = "Le login est obligatoire")
    private String login;
 
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getLogin() {
        return login;
    }
 
    public void setLogin(String login) {
        this.login = login;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
}
 