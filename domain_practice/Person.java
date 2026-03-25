package domain_practice;

public abstract class Person extends BaseEntity {
    private String name;
    private String email;

    protected Person(String name, String email) {
        super();
        setName(name);
        setEmail(email);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public abstract String getRole();

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty.");
        }
        this.name = name;
    }

    private void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid Email.");
        }
        this.email = email;
    }
}
