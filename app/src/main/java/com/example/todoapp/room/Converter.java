package com.example.todoapp.room;

import androidx.room.TypeConverter;
import com.example.todoapp.model.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converter {
    @TypeConverter
    public static List<Contact> fromString(String value) {
        Type listType = new TypeToken<List<Contact>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Contact> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
