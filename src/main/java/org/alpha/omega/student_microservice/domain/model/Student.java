package org.alpha.omega.student_microservice.domain.model;

import java.util.Objects;

/**
 * Student class is immutable, because not define setter methods. This is very clean, by:
 * - Thread safety
 * - Predictability
 * - Better functional design
 * - Best for reactive programming
 */
public class Student {

    private Integer id;
    private String name;
    private String lastName;
    private Boolean status;
    private Integer age;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
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
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", age=" + age +
                '}';
    }

    public static class StudentBuilder {

        private Integer id;
        private String name;
        private String lastName;
        private Boolean status;
        private Integer age;

        private StudentBuilder() {}

        public StudentBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public StudentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public StudentBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder status(Boolean status) {
            this.status = status;
            return this;
        }

        public StudentBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public Student build() {
            Student student = new Student();
            student.id = this.id;
            student.name = this.name;
            student.lastName = this.lastName;
            student.status = this.status;
            student.age = this.age;
            return student;
        }
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }
}
