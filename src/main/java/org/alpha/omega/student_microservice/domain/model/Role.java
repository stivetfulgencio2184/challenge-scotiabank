package org.alpha.omega.student_microservice.domain.model;

import java.util.Objects;

public class Role {

    private Integer id;
    private String name;
    private String abbreviation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id == null) {
            return false;
        } else return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }

    public static class RoleBuilder {

        private Integer id;
        private String name;
        private String abbreviation;

        private RoleBuilder() {}

        public RoleBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public RoleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoleBuilder abbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
            return this;
        }

        public Role build() {
            Role role = new Role();
            role.id = this.id;
            role.name = this.name;
            role.abbreviation = this.abbreviation;
            return role;
        }
    }

    public static RoleBuilder builder() {
        return new RoleBuilder();
    }
}
