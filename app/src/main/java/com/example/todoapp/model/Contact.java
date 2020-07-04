package com.example.todoapp.model;

import androidx.room.Entity;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Contact implements Serializable {

    private String id;
    private String name;
    private String tel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + name + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id &&
                Objects.equals(name, contact.name) &&
                Objects.equals(tel, contact.tel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tel);
    }
}
