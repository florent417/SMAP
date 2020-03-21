package model;

public class Movie {
    private String name;
    private int year;
    private String role;

    public Movie(String name, int year, String role) {
        this.name = name;
        this.year = year;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
