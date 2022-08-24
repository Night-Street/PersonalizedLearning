package entities;

public class User {

    private Integer id;
    private String name;
    private String password;
    private String firstname;
    private String secondname;
    private String email;
    private Roleinfo roleId;

    public int getNameLength() {
        return 32;
    }

    public int getThirdLogin() {
        return 100;
    }

    public int getPasswordLength() {
        return 128;
    }

    public int getFirstnameLength() {
        return 32;
    }

    public int getSecondnameLength() {
        return 32;
    }

    public int getEmailLength() {
        return 30;
    }

    public int getPhoneLength() {
        return 20;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roleinfo getRoleId() {
        return roleId;
    }

    public void setRoleId(Roleinfo roleId) {
        this.roleId = roleId;
    }
}